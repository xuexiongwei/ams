package com.xxw.springcloud.ams.util.dxf.coodinateCover;

public class GaussProjection
{
  private double _M0;
  private double _M2;
  private double _M4;
  private double _M6;
  private double _M8;
  private double _A0;
  private double _A2;
  private double _A4;
  private double _A6;
  private double _A8;
  private double _X;
  private double _Y;
  private double _Lat;
  private double _Lng;
  private int _Maridian;
  private double _l;
  private int _ZoneWide;
  private String _ErrMsg;
  double a = 0.0D;
  double b;
  double e1 = 0.0D; double e2 = 0.0D; double f = 0.0D;

  public GaussProjection(EnumEllipseSys ellipse, int zoneWide)
  {
    this._ZoneWide = zoneWide;
    switch (ellipse)
    {
    case WGS84:
      this.a = 6378245.0D;
      this.b = 6356863.0187730473D;

      this.e1 = (Math.sqrt(Math.pow(this.a, 2.0D) - Math.pow(this.b, 2.0D)) / this.a);
      this.e2 = (Math.sqrt(Math.pow(this.a, 2.0D) - Math.pow(this.b, 2.0D)) / this.b);
      break;
    case BeiJing54:
      this.a = 6378137.0D;
      this.b = 6356752.3141999999D;
      this.e1 = (Math.sqrt(Math.pow(this.a, 2.0D) - Math.pow(this.b, 2.0D)) / this.a);
      this.e2 = (Math.sqrt(Math.pow(this.a, 2.0D) - Math.pow(this.b, 2.0D)) / this.b);
      break;
    case XiAn80:
      this.a = 6378140.0D;
      this.b = 6356755.2881575283D;
      this.e1 = (Math.sqrt(Math.pow(this.a, 2.0D) - Math.pow(this.b, 2.0D)) / this.a);
      this.e2 = (Math.sqrt(Math.pow(this.a, 2.0D) - Math.pow(this.b, 2.0D)) / this.b);

      break;
    }

    this.f = ((this.a - this.b) / this.a);
    CalMidPara();
  }

  public Boolean Projection(double B, double L, double H)
  {
    Boolean bRtn = Boolean.valueOf(true);
    try
    {
      int n = 0;

      if (this._ZoneWide == 6)
      {
        n = (int)L / 6;
        if (L % 6.0D > 0.0D)
        {
          n++;
        }
        this._Maridian = (6 * n - 3);
        this._l = (L - this._Maridian);
      }
      if (3 == this._ZoneWide)
      {
        n = (int)(L - 1.5D) / 3;
        if ((L - 1.5D) % 3.0D > 0.0D)
        {
          n++;
        }
        this._Maridian = (3 * n);
        this._l = (L - this._Maridian);
      }

      B = B * 3.141592653589793D / 180.0D;
      L = L * 3.141592653589793D / 180.0D;
      this._l = (this._l * 3.141592653589793D / 180.0D);

      double X = this._A0 * B - this._A2 * Math.sin(2.0D * B) / 2.0D + this._A4 * Math.sin(4.0D * B) / 4.0D - 
        this._A6 * Math.sin(6.0D * B) / 6.0D + this._A8 * Math.sin(8.0D * B) / 8.0D;

      double T = Math.tan(B);
      double ita = this.e2 * Math.cos(B);
      double SinB = Math.sin(B);
      double CosB = Math.cos(B);

      double N = this.a * Math.pow(1.0D - Math.pow(this.e1 * SinB, 2.0D), -0.5D);
      this._X = 
        (X + N * T * Math.pow(CosB, 2.0D) * Math.pow(this._l, 2.0D) / 2.0D + 
        N * T * (5.0D - Math.pow(T, 2.0D) + 9.0D * Math.pow(ita, 2.0D) + 
        4.0D * Math.pow(ita, 2.0D)) * Math.pow(CosB * this._l, 4.0D) / 24.0D + 
        N * T * (61.0D - 58.0D * Math.pow(T, 2.0D) + 
        Math.pow(T, 4.0D)) * Math.pow(CosB * this._l, 6.0D) / 720.0D);
      this._Y = (N * CosB * this._l + N * (1.0D - Math.pow(T, 2.0D) + Math.pow(ita, 2.0D)) * Math.pow(CosB * this._l, 3.0D) / 6.0D + N * (5.0D - 18.0D * Math.pow(T, 2.0D) + Math.pow(T, 4.0D) + 14.0D * Math.pow(ita, 2.0D) - 58.0D * Math.pow(ita * T, 2.0D)) * Math.pow(CosB * this._l, 5.0D) / 120.0D);
    }
    catch (Exception ex)
    {
      this._ErrMsg = ex.getMessage();
      bRtn = Boolean.valueOf(false);
    }
    return bRtn;
  }

  public Boolean AntiProjection(double x, double y, int maridian)
  {
    this._Maridian = maridian;
    boolean bRtn = true;
    try
    {
      double Bf0 = x / this._A0;
      double Bf = GetBf(Bf0, x);

      double Nf = this.a * Math.pow(1.0D - Math.pow(this.e1 * Math.sin(Bf), 2.0D), -0.5D);
      double Z = y / (Nf * Math.cos(Bf));

      double Mf = this.a * (1.0D - this.e1 * this.e1) * 
        Math.pow(1.0D - Math.pow(this.e1 * Math.sin(Bf), 2.0D), -1.5D);
      double Tf = Math.tan(Bf);
      double ITAf2 = Math.pow(this.e2 * Math.cos(Bf), 2.0D);

      double B2 = Tf * Math.pow(y, 2.0D) / (2.0D * Mf * Nf);
      double B4 = Tf * Math.pow(y, 4.0D) * (5.0D + 3.0D * Math.pow(Tf, 2.0D) + ITAf2 - 9.0D * Math.pow(Tf, 2.0D) * ITAf2) / (24.0D * Mf * Math.pow(Nf, 3.0D));
      double B6 = Tf * Math.pow(y, 6.0D) * (61.0D + 90.0D * Math.pow(Tf, 2.0D) + 45.0D * Math.pow(Tf, 4.0D)) / (720.0D * Mf * Math.pow(Nf, 5.0D));

      double B3 = (1.0D + 2.0D * Math.pow(Tf, 2.0D) + ITAf2) * Math.pow(y, 3.0D) / (6.0D * Math.pow(Nf, 3.0D) * Math.cos(Bf));
      double B5 = (5.0D + 28.0D * Math.pow(Tf, 2.0D) + 24.0D * Math.pow(Tf, 4.0D) + 6.0D * ITAf2 + 8.0D * ITAf2) * Math.pow(y, 5.0D) / (120.0D * Math.pow(Nf, 5.0D) * Math.cos(Bf));

      this._Lat = ((Bf - B2 + B4 - B6) * 180.0D / 3.141592653589793D);
      this._l = ((Z - B3 + B5) * 180.0D / 3.141592653589793D);
    }
    catch (Exception ex)
    {
      this._ErrMsg = (this._ErrMsg + "��˹ͶӰ�������" + ex.getMessage());
      bRtn = false;
    }
    return Boolean.valueOf(bRtn);
  }

  private double GetBf(double bf, double x)
  {
    double M = -this._A2 * Math.sin(2.0D * bf) / 2.0D + this._A4 * Math.sin(4.0D * bf) / 4.0D - 
      this._A6 * Math.sin(6.0D * bf) / 6.0D + this._A8 * Math.sin(8.0D * this.f) / 8.0D;
    double Bf = (x - M) / this._A0;
    if (Math.abs(bf - Bf) > 3.141592653589793D * Math.pow(10.0D, -8.0D) / 648.0D) {
      Bf = GetBf(Bf, x);
    }
    return Bf;
  }

  private void CalMidPara()
  {
    this._M0 = (this.a * (1.0D - Math.pow(this.e1, 2.0D)));
    this._M2 = (3.0D * Math.pow(this.e1, 2.0D) * this._M0 / 2.0D);
    this._M4 = (5.0D * Math.pow(this.e1, 2.0D) * this._M2 / 4.0D);
    this._M6 = (7.0D * Math.pow(this.e1, 2.0D) * this._M4 / 6.0D);
    this._M8 = (9.0D * Math.pow(this.e1, 2.0D) * this._M6 / 8.0D);

    this._A0 = (this._M0 + this._M2 / 2.0D + 3.0D * this._M4 / 8.0D + 5.0D * this._M6 / 16.0D + 35.0D * this._M8 / 128.0D);
    this._A2 = (this._M2 / 2.0D + this._M4 / 2.0D + 15.0D * this._M6 / 32.0D + 7.0D * this._M8 / 16.0D);
    this._A4 = (this._M4 / 8.0D + this._M6 * 3.0D / 16.0D + this._M8 * 7.0D / 32.0D);
    this._A6 = (this._M6 / 32.0D + this._M8 / 16.0D);
    this._A8 = (this._M8 / 128.0D);
  }

  public double get_X()
  {
    return this._X;
  }

  public double get_Y()
  {
    return this._Y;
  }

  public double get_Lat()
  {
    return this._Lat;
  }

  public double get_Lng()
  {
    return this._Lng;
  }

  public String get_ErrMsg()
  {
    return this._ErrMsg;
  }

  public double get_L()
  {
    return this._l;
  }

  public int get_Maridian()
  {
    return this._Maridian;
  }
}