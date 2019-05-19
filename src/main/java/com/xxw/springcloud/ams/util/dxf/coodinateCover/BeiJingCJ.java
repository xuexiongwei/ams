package com.xxw.springcloud.ams.util.dxf.coodinateCover;

import com.xxw.springcloud.ams.util.dxf.coodinateCover.FPData;
import com.xxw.springcloud.ams.util.dxf.coodinateCover.LocalCoordinateSys;

public class BeiJingCJ extends LocalCoordinateSys
{
  private FPData _FP117;
  private FPData _FP114;
  private FPData _AntiFP117;
  private FPData _AntiFP114;

  public BeiJingCJ()
  {
    this._FP117 = new FPData(-4114189.43005706D, 587644.93103613402D, 0.999965824623319D, -0.00727323158104647D, "-0:25:0.211702854812335", 117);
    this._FP114 = new FPData(-4110248.18816924D, 181646.87498660799D, 0.999959721722356D, 0.0265584970928546D, "1:31:18.0832570717289", 114);

    this._AntiFP117 = new FPData(4118495.4015565501D, -557725.25972793996D, 1.00003417654468D, 0.00727323158104643D, "0:25:0.211702854803941", 117);
    this._AntiFP114 = new FPData(4104140.2908000001D, -290743.70860000001D, 1.00004027944342D, -0.0265584970928545D, "-1:31:18.0832570717057", 114);
  }

  public FPData getFPData(boolean positive, int maridian)
  {
    if (maridian == 117)
    {
      if (positive)
      {
        return this._FP117;
      }

      return this._AntiFP117;
    }

    if (maridian == 114)
    {
      if (positive)
      {
        return this._FP114;
      }

      return this._AntiFP114;
    }

    return null;
  }
}