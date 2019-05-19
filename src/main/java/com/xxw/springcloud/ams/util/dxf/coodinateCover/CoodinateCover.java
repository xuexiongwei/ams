package com.xxw.springcloud.ams.util.dxf.coodinateCover;
 
import java.io.PrintStream;
 
public class CoodinateCover
{
  private GaussProjection _GP;
  private double _X;
  private double _Y;
  private double _B;
  private double _L;
  private String _ErrMsg;
  private EnumLocalCoordinateSys _lcs;
  
  public static void main(String[] args)
  {
    CoodinateCover CC = new CoodinateCover(3, EnumEllipseSys.WGS84, EnumLocalCoordinateSys.BeiJingCJ);
    if (!CC.FunCover(39.976732200000001D, 116.41657351000001D).booleanValue()) {
      System.out.println("���ִ���" + CC.get_ErrMsg());
    }
    System.out.println("��γ�����꣨40.2321880683303, 116.523973513395��ת��Ϊ�ǽ����꣨" + CC.get_X() + ",Y���꣺" + CC.get_Y() + "��");
    if (!CC.FunAntiCover(CC.get_X(), CC.get_Y()).booleanValue()) {
      System.out.println("���ִ���" + CC.get_ErrMsg());
    }
    System.out.println("��ת��Ϊ��γ��Ϊ��" + CC.get_B() + "������Ϊ��" + CC.get_L());
  }
  
  public CoodinateCover(int zoneWide, EnumEllipseSys ellipse, EnumLocalCoordinateSys lcs)
  {
    this._lcs = lcs;
    this._GP = new GaussProjection(ellipse, zoneWide);
  }
  
  public Boolean FunCover(double B, double L)
  {
    if (!this._GP.Projection(B, L, 0.0D).booleanValue())
    {
      this._ErrMsg = ("��˹ͶӰʧ�ܣ�" + this._GP.get_ErrMsg());
      return Boolean.valueOf(false);
    }
    FPData oFPData;
    switch (this._lcs)
    {
    case BeiJingCJ: 
      LocalCoordinateSys LCS = new BeiJingCJ();
      oFPData = LCS.getFPData(true, this._GP.get_Maridian());
      break;
    default: 
      oFPData = null;
    }
    if (oFPData == null)
    {
      this._ErrMsg = "û��ʵ����FPData����";
      return Boolean.valueOf(false);
    }
    this._X = (oFPData.get_X() + oFPData.get_K() * this._GP.get_X() * Math.cos(oFPData.get_T()) - oFPData.get_K() * this._GP.get_Y() * Math.sin(oFPData.get_T()));
    this._Y = (oFPData.get_Y() + oFPData.get_K() * this._GP.get_X() * Math.sin(oFPData.get_T()) + oFPData.get_K() * this._GP.get_Y() * Math.cos(oFPData.get_T()));
    return Boolean.valueOf(true);
  }
  
  public Boolean FunAntiCover(double x, double y)
  {
    Boolean bRtn = Boolean.valueOf(true);
    switch (this._lcs)
    {
    case BeiJingCJ: 
      bRtn = Boolean.valueOf(BeiJintCJToWGS84(x, y));
      break;
    default: 
      bRtn = Boolean.valueOf(false);
    }
    return bRtn;
  }
  
  private boolean BeiJintCJToWGS84(double x, double y)
  {
    boolean bRtn = true;
    LocalCoordinateSys LCS = new BeiJingCJ();
    FPData oFPData = LCS.getFPData(false, 117);
    this._X = (oFPData.get_X() + oFPData.get_K() * x * Math.cos(oFPData.get_T()) - oFPData.get_K() * y * Math.sin(oFPData.get_T()));
    this._Y = (oFPData.get_Y() + oFPData.get_K() * x * Math.sin(oFPData.get_T()) + oFPData.get_K() * y * Math.cos(oFPData.get_T()));
    if (!this._GP.AntiProjection(this._X, this._Y, 117).booleanValue())
    {
      this._ErrMsg = ("��˹ͶӰʧ�ܣ�" + this._GP.get_ErrMsg());
      bRtn = false;
      return bRtn;
    }
    if ((this._GP.get_L() < 1.5D) && (this._GP.get_L() > -1.5D))
    {
      this._L = (117.0D + this._GP.get_L());
      bRtn = true;
    }
    else if (this._GP.get_L() > 1.5D)
    {
      this._B = this._GP.get_Lat();
      this._L = (117.0D + this._GP.get_L());
      this._ErrMsg = "����ͶӰ��Χ�����ܲ�׼ȷ";
      bRtn = false;
    }
    else
    {
      FPData oFPData2 = LCS.getFPData(false, 114);
      this._X = (oFPData2.get_X() + oFPData2.get_K() * x * Math.cos(oFPData2.get_T()) - oFPData2.get_K() * y * Math.sin(oFPData2.get_T()));
      this._Y = (oFPData2.get_Y() + oFPData2.get_K() * x * Math.sin(oFPData2.get_T()) + oFPData2.get_K() * y * Math.cos(oFPData2.get_T()));
      if (!this._GP.AntiProjection(this._X, this._Y, 114).booleanValue())
      {
        this._ErrMsg = ("��˹ͶӰʧ�ܣ�" + this._GP.get_ErrMsg());
        bRtn = false;
      }
      this._L = (114.0D + this._GP.get_L());
      bRtn = true;
    }
    this._B = this._GP.get_Lat();
    return bRtn;
  }
  
  public double get_X()
  {
    return this._X;
  }
  
  public double get_Y()
  {
    return this._Y;
  }
  
  public double get_B()
  {
    return this._B;
  }
  
  public double get_L()
  {
    return this._L;
  }
  
  public String get_ErrMsg()
  {
    return this._ErrMsg;
  }
}
