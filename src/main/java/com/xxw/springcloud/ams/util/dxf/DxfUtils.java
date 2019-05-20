package com.xxw.springcloud.ams.util.dxf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.xxw.springcloud.ams.model.DxfEntity;
import com.xxw.springcloud.ams.util.FastList;
import com.xxw.springcloud.ams.util.dxf.coodinateCover.CoodinateCover;
import com.xxw.springcloud.ams.util.dxf.coodinateCover.Coordinate;
import com.xxw.springcloud.ams.util.dxf.coodinateCover.CoordinateSys;
import com.xxw.springcloud.ams.util.dxf.coodinateCover.EnumEllipseSys;
import com.xxw.springcloud.ams.util.dxf.coodinateCover.EnumLocalCoordinateSys;

public class DxfUtils {

	private static Logger logger = LoggerFactory.getLogger(DxfUtils.class);

	public final static double jc = 0.0064;
	public final static double wc = 0.0070;

	public static Double[] getAinCoordinate(String coordx, String coordy) {
		Double[] returnCoordinate = new Double[2];
		if (coordx == null || coordy == null) {
			return null;
		}

		CoodinateCover cc = new CoodinateCover(3, EnumEllipseSys.WGS84, EnumLocalCoordinateSys.BeiJingCJ);
		if (!cc.FunAntiCover(Double.valueOf(coordy).doubleValue(), Double.valueOf(coordx).doubleValue())) {
			try {
				throw new RuntimeException(cc.get_ErrMsg());
			} catch (Exception e) {
				System.out.println("捕获转换异常：" + e);
			}
		}

		if (cc.get_L() != 0.0D && cc.get_B() != 0.0D) {
			Coordinate _Coordinate = new Coordinate(cc.get_B(), cc.get_L(), CoordinateSys.WGS84);
			if (_Coordinate.WGS84ToGCJ02()) {
				returnCoordinate[0] = _Coordinate.get_GCJLat();
				returnCoordinate[1] = _Coordinate.get_GCJLng();
			} else {
				returnCoordinate[0] = cc.get_L();
				returnCoordinate[1] = cc.get_B();
			}

		} else {
			returnCoordinate[0] = cc.get_L();
			returnCoordinate[1] = cc.get_B();
		}
		return returnCoordinate;
	}

	/**
	 * 解析並保存
	 */
	public static List<DxfEntity> analysis(MultipartFile file, String prjSN) {

		List<DxfEntity> beans = FastList.newInstance();

		try {
			InputStream inputStream = file.getInputStream();
			InputStreamReader is = new InputStreamReader(inputStream);
			BufferedReader br = new BufferedReader(is);

			List<LWpolyline> list = Readdxf01.readEntities(br);
			int i = 0;
			for (LWpolyline lWpolyline : list) {
				Point[] ps = lWpolyline.getNum();
				DxfEntity entity = new DxfEntity();
				entity.setConstrID(Long.parseLong(i + ""));// 图元ID
				StringBuffer v = new StringBuffer();// 经纬度
				for (Point p : ps) {
					Double[] dd = DxfUtils.getAinCoordinate(p.getX() + "", p.getY() + "");
					v.append((dd[1] + jc) + "," + (dd[0] + wc) + "|");
				}
				entity.setLonglatV(v.toString());// 经纬度
				entity.setPrjSN(prjSN);
				beans.add(entity);
				i++;
			}
		} catch (Exception e) {
			logger.error("解析dxf 文件失败！", e);
			throw new RuntimeException("解析dxf 文件失败！" + e.getMessage());
		}
		return beans;
	}

	public static void main(String[] args) {

		try {
			File file = new File("F:\\xjbl.DXF");
			FileReader fr = new FileReader(file);
			BufferedReader bfr = new BufferedReader(fr);
			List<LWpolyline> list = Readdxf01.readEntities(bfr);
			int i = 0;
			for (LWpolyline lWpolyline : list) {
				Point[] ps = lWpolyline.getNum();
				String map = "var points" + i + " = [";
				for (Point p : ps) {
					Double[] dd = DxfUtils.getAinCoordinate(p.getX() + "", p.getY() + "");
					map = map + "new BMap.Point(" + dd[1] + "," + dd[0] + "),";
				}
				map = map + "]";

				i++;

				System.out.println(map);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}