package com.xxw.springcloud.ams.util.dxf.coodinateCover;

public class FPData
{
  private double _k;
  private double _x;
  private double _y;
  private double _t;
  private String _t1;
  private int _Maridian;

  public FPData(double x, double y, double k, double t, String t1, int maridian)
  {
    this._k = k;
    this._x = x;
    this._y = y;
    this._t = t;
    this._t1 = t1;
    this._Maridian = maridian;
  }

  public double get_K()
  {
    return this._k;
  }

  public double get_X()
  {
    return this._x;
  }

  public double get_T()
  {
    return this._t;
  }

  public double get_Y()
  {
    return this._y;
  }

  public int get_Maridian()
  {
    return this._Maridian;
  }

  public String get_T1()
  {
    return this._t1;
  }
}