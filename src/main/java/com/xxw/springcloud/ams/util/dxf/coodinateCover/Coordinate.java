package com.xxw.springcloud.ams.util.dxf.coodinateCover;

public class Coordinate
{
  private double _GCJLat = 0.0D;
  private double _GCJLng = 0.0D;
  private double _WGSLat = 0.0D;
  private double _WGSLng = 0.0D;
  private double _BDLat = 0.0D;
  private double _BDLng = 0.0D;
  private String _ErrMsg;
  double a = 6378245.0D;
  double ee = 0.006693421622965943D;

  public static void main(String[] args)
  {
    Coordinate Crd = new Coordinate(39.600000000000001D, 116.59999999999999D, CoordinateSys.WGS84);
    Crd.WGS84ToGCJ02();
  }

  public Coordinate(double lat, double lng, CoordinateSys crdSys)
  {
    if (crdSys == CoordinateSys.WGS84)
    {
      this._WGSLat = lat;
      this._WGSLng = lng;
    }
    else if (crdSys == CoordinateSys.GCJ02)
    {
      this._GCJLat = lat;
      this._GCJLng = lng;
    }
    else
    {
      this._BDLat = lat;
      this._BDLng = lng;
    }
  }

  private double TransformLat(double x, double y)
  {
    double lat = -100.0D + 2.0D * x + 3.0D * y + 0.2D * y * y + 0.1D * x * y + 
      0.2D * Math.sqrt(Math.abs(x));

    lat = lat + (20.0D * Math.sin(6.0D * x * 3.141592653589793D) + 20.0D * Math.sin(2.0D * x * 
      3.141592653589793D)) * 2.0D / 3.0D;

    lat = lat + (20.0D * Math.sin(y * 3.141592653589793D) + 40.0D * Math.sin(y / 3.0D * 
      3.141592653589793D)) * 2.0D / 3.0D;

    lat = lat + (160.0D * Math.sin(y / 12.0D * 3.141592653589793D) + 320.0D * Math.sin(y * 
      3.141592653589793D / 30.0D)) * 2.0D / 3.0D;
    return lat;
  }

  private double TransformLng(double x, double y)
  {
    double lng = 300.0D + x + 2.0D * y + 0.1D * x * x + 0.1D * x * y + 0.1D * 
      Math.sqrt(Math.abs(x));

    lng = lng + (20.0D * Math.sin(6.0D * x * 3.141592653589793D) + 20.0D * Math.sin(2.0D * x * 
      3.141592653589793D)) * 2.0D / 3.0D;

    lng = lng + (20.0D * Math.sin(x * 3.141592653589793D) + 40.0D * Math.sin(x / 3.0D * 
      3.141592653589793D)) * 2.0D / 3.0D;

    lng = lng + (150.0D * Math.sin(x / 12.0D * 3.141592653589793D) + 300.0D * Math.sin(x / 
      30.0D * 3.141592653589793D)) * 2.0D / 3.0D;
    return lng;
  }

  private static Boolean OutChina(double lat, double lng)
  {
    if ((lng < 72.004000000000005D) || (lng > 137.8347D))
      return Boolean.valueOf(true);
    if ((lat < 0.8293D) || (lat > 55.827100000000002D))
      return Boolean.valueOf(true);
    return Boolean.valueOf(false);
  }

  public Boolean WGS84ToGCJ02()
  {
    if (OutChina(this._WGSLat, this._WGSLng).booleanValue())
    {
      this._ErrMsg = "���곬���й���Χ��";
      return Boolean.valueOf(false);
    }

    try
    {
      double dLat = TransformLat(this._WGSLng - 105.0D, this._WGSLat - 35.0D);
      double dLng = TransformLng(this._WGSLng - 105.0D, this._WGSLat - 35.0D);

      double radLat = this._WGSLat * 3.141592653589793D / 180.0D;
      double magic = Math.sin(radLat);
      magic = 1.0D - this.ee * magic * magic;
      double sqrtMagic = Math.sqrt(magic);
      dLat = dLat * 180.0D / (
        this.a * (1.0D - this.ee) / (magic * sqrtMagic) * 3.141592653589793D);
      dLng = dLng * 180.0D / (this.a / sqrtMagic * Math.cos(radLat) * 3.141592653589793D);
      this._GCJLat = (this._WGSLat + dLat);
      this._GCJLng = (this._WGSLng + dLng);
    }
    catch (Exception ex)
    {
      this._ErrMsg = ("WGS84ToGCJ02����" + ex.getMessage());
      return Boolean.valueOf(false);
    }
    return Boolean.valueOf(true);
  }

  public Boolean WGS84ToBD09()
  {
    if (OutChina(this._WGSLat, this._WGSLng).booleanValue())
    {
      this._ErrMsg = "���곬���й���Χ��";
      return Boolean.valueOf(false);
    }
    try
    {
      if (!WGS84ToGCJ02().booleanValue())
      {
        return Boolean.valueOf(false);
      }

      return GCJ02ToBD09();
    }
    catch (Exception ex)
    {
      this._ErrMsg = ("WGS84ToBD09����" + ex.getMessage());
    }return Boolean.valueOf(false);
  }

  public Boolean GCJ02ToWGS84()
  {
    if (OutChina(this._GCJLat, this._GCJLng).booleanValue())
    {
      this._ErrMsg = "���곬���й���Χ��";
      return Boolean.valueOf(false);
    }

    try
    {
      double dLat = TransformLat(this._GCJLng - 105.0D, this._GCJLat - 35.0D);
      double dLng = TransformLng(this._GCJLng - 105.0D, this._GCJLat - 35.0D);
      double radLat = this._GCJLat / 180.0D * 3.141592653589793D;
      double magic = Math.sin(radLat);
      magic = 1.0D - this.ee * magic * magic;
      double sqrtMagic = Math.sqrt(magic);
      dLat = dLat * 180.0D / (this.a * (1.0D - this.ee) / (magic * sqrtMagic) * 3.141592653589793D);
      dLng = dLng * 180.0D / (this.a / sqrtMagic * Math.cos(radLat) * 3.141592653589793D);
      double mgLat = this._GCJLat + dLat;
      double mgLng = this._GCJLng + dLng;
      this._WGSLng = (this._GCJLng * 2.0D - mgLng);
      this._WGSLat = (this._GCJLat * 2.0D - mgLat);
    }
    catch (Exception ex)
    {
      this._ErrMsg = ("GCJ02ToWGS84����" + ex.getMessage());
      return Boolean.valueOf(false);
    }

    return Boolean.valueOf(true);
  }

  public Boolean GCJ02ToBD09()
  {
    if (OutChina(this._GCJLat, this._GCJLng).booleanValue())
    {
      this._ErrMsg = "���곬���й���Χ��";
      return Boolean.valueOf(false);
    }
    try
    {
      double x = this._GCJLng; double y = this._GCJLat;
      double z = Math.sqrt(x * x + y * y) + 2.E-005D * Math.sin(y * 3.141592653589793D);
      double theta = Math.atan2(y, x) + 3.E-006D * Math.cos(x * 3.141592653589793D);
      this._BDLng = (z * Math.cos(theta) + 0.0065D);
      this._BDLat = (z * Math.sin(theta) + 0.006D);
    }
    catch (Exception ex)
    {
      this._ErrMsg = ("GCJ02ToBD09����" + ex.getMessage());
      return Boolean.valueOf(false);
    }

    return Boolean.valueOf(true);
  }

  public Boolean BD09ToGCJ02()
  {
    if (OutChina(this._BDLat, this._BDLng).booleanValue())
    {
      this._ErrMsg = "���곬���й���Χ��";
      return Boolean.valueOf(false);
    }
    try
    {
      double x = this._BDLng - 0.0065D; double y = this._BDLat - 0.006D;
      double z = Math.sqrt(x * x + y * y) - 2.E-005D * Math.sin(y * 3.141592653589793D);
      double theta = Math.atan2(y, x) - 3.E-006D * Math.cos(x * 3.141592653589793D);
      this._GCJLng = (z * Math.cos(theta));
      this._GCJLat = (z * Math.sin(theta));
    }
    catch (Exception ex)
    {
      this._ErrMsg = ("BD09ToGCJ02����" + ex.getMessage());
      return Boolean.valueOf(false);
    }
    return Boolean.valueOf(true);
  }

  public Boolean BD09ToGWGS84()
  {
    if (OutChina(this._BDLat, this._BDLng).booleanValue())
    {
      this._ErrMsg = "���곬���й���Χ��";
      return Boolean.valueOf(false);
    }
    try
    {
      if (!BD09ToGCJ02().booleanValue())
        return Boolean.valueOf(false);
      return GCJ02ToWGS84();
    }
    catch (Exception ex)
    {
      this._ErrMsg = ("BD09ToGWGS84����" + ex.getMessage());
    }return Boolean.valueOf(false);
  }

  public double get_GCJLat()
  {
    return this._GCJLat;
  }

  public double get_GCJLng() {
    return this._GCJLng;
  }

  public double get_WGSLat() {
    return this._WGSLat;
  }

  public double get_WGSLng() {
    return this._WGSLng;
  }

  public double get_BDLat() {
    return this._BDLat;
  }

  public double get_BDLng() {
    return this._BDLng;
  }

  public String get_ErrMsg() {
    return this._ErrMsg;
  }
}