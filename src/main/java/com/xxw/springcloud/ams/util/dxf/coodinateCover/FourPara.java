package com.xxw.springcloud.ams.util.dxf.coodinateCover;

import Jama.Matrix;

public class FourPara
{
  private double _OX0;
  private double _OY0;
  private double _OX1;
  private double _OY1;
  private double _TX0;
  private double _TY0;
  private double _TX1;
  private double _TY1;
  private double _X;
  private double _Y;
  private double _K;
  private double _T;
  private String _T1;
  private String _ErrMsg = "";

  public FourPara(double pOX0, double pOY0, double pOX1, double pOY1, double pTX0, double pTY0, double pTX1, double pTY1)
  {
    this._TX1 = pTX1;
    this._TY1 = pTY1;
    this._OX0 = pOX0;
    this._OY0 = pOY0;
    this._OX1 = pOX1;
    this._OY1 = pOY1;
    this._TX0 = pTX0;
    this._TY0 = pTY0;
  }

  public Boolean CalFourPara()
  {
    Boolean bRtn = Boolean.valueOf(true);
    try {
      double[][] lArr = { { 1.0D, 0.0D, this._OX0, -1.0D * this._OY0 }, 
        { 0.0D, 1.0D, this._OY0, this._OX0 }, { 1.0D, 0.0D, this._OX1, -1.0D * this._OY1 }, 
        { 0.0D, 1.0D, this._OY1, this._OX1 } };
      Matrix A = new Matrix(lArr);

      double[][] rArr = { { this._TX0 }, { this._TY0 }, { this._TX1 }, 
        { this._TY1 } };
      Matrix B = new Matrix(rArr);
      Matrix R = A.solve(B);

      this._X = R.get(0, 0);
      this._Y = R.get(1, 0);
      double c = R.get(2, 0);
      double d = R.get(3, 0);

      if ((c == 0.0D) && (d > 0.0D))
      {
        this._K = d;
        this._T = 1.570796326794897D;
      }
      else if ((c == 0.0D) && (d < 0.0D))
      {
        this._K = (-d);
        this._T = 4.71238898038469D;
      }
      else if ((d == 0.0D) && (c > 0.0D))
      {
        this._K = c;
        this._T = 0.0D;
      }
      else if ((d == 0.0D) && (c < 0.0D))
      {
        this._K = (-c);
        this._T = 3.141592653589793D;
      } else {
        this._T = Math.atan(d / c);
        this._K = (d / Math.sin(this._T));

        double degree = this._T * 180.0D / 3.141592653589793D;
        double dg = 0.0D;
        double m = 0.0D;
        double s = 0.0D;

        if (this._T > 0.0D) {
          dg = Math.floor(degree);
          m = Math.floor((degree - dg) * 60.0D);
          s = (degree - dg - m / 60.0D) * 3600.0D;
          this._T1 = (dg + ":" + m + ":" + s);
        } else {
          degree = -degree;
          dg = Math.floor(degree);
          m = Math.floor((degree - dg) * 60.0D);
          s = (degree - dg - m / 60.0D) * 3600.0D;
          this._T1 = ("-" + dg + ":" + m + ":" + s);
        }
      }

    }
    catch (Exception e)
    {
      this._ErrMsg = e.getMessage();
      bRtn = Boolean.valueOf(false);
    }

    return bRtn;
  }

  public double get_Y() {
    return this._Y;
  }

  public double get_X() {
    return this._X;
  }

  public double get_K() {
    return this._K;
  }

  public double get_T() {
    return this._T;
  }

  public String get_T1() {
    return this._T1;
  }

  public String get_ErrMsg() {
    return this._ErrMsg;
  }
}