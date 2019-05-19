package com.xxw.springcloud.ams.util.dxf;

//import org.apache.commons.math3.util.FastMath;
/**
 *
 * @author User
 */

abstract class tuys {
	String name;
}

class Line extends tuys {
	Point qd, zd;
	double zb[] = new double[4];

	// String name="Line";
	public Line(Point a, Point b) {
		qd = new Point(a);
		zd = new Point(b);
		// name="Line";
		zb[0] = qd.x;
		zb[1] = qd.y;
		zb[2] = zd.x;
		zb[3] = zd.y;
	}

	public Line(double x1, double y1, double x2, double y2) {
		qd = new Point();
		zd = new Point();
		qd.x = x1;
		qd.y = y1;
		zd.x = x2;
		zd.y = y2;
		// name="Line";
		zb[0] = qd.x;
		zb[1] = qd.y;
		zb[2] = zd.x;
		zb[3] = zd.y;
	}

	public Line(Line sl) {
		qd = new Point(sl.qd);
		zd = new Point(sl.zd);
		zb[0] = qd.x;
		zb[1] = qd.y;
		zb[2] = zd.x;
		zb[3] = zd.y;
	}

	public void whzb() {// �������յ�ά�����������
		zb[0] = qd.x;
		zb[1] = qd.y;
		zb[2] = zd.x;
		zb[3] = zd.y;
	}
}

class Arc extends tuys {
	Point center;
	double radius, qhd, zhd;// �뾶����㻡�ȣ��յ㻡
	Point qd, td, zd;// ��㡢�����㡢�յ�
	double zb[] = new double[6];// ���xy���꣬˳ʱ��洢��CAD�е���㵽�յ�һ��Ϊ��ʱ�뷽����Ӧ�ȴ��յ�
								// //ע�������Ҫͨ�����캯����ά��������飬����������Ļ���
	/*
	 * public Arc(Point a,Point b,Point c){//��㡢�����㡢�յ�˳���ʼ�� qd=new Point(a);
	 * td=new Point(b); zd=new Point(c); zb=new double[6];
	 * zb[0]=zd.x;zb[1]=zd.y;zb[2]=td.x;zb[3]=td.y;zb[4]=qd.x;zb[5]=qd.y;
	 * //������˳ʱ��� ����˳ʱ����ʱ�����û�й�ϵ����Ϊ������ȷ����һ�����������з�����졣
	 * //zb[0]=qd.x;zb[1]=qd.y;zb[2]=td.x;zb[3]=td.y;zb[4]=zd.x;zb[5]=zd.y;
	 * name="Arc"; }
	 */

	public Arc(Arc sac) {
		this(sac.center, sac.radius, sac.qhd, sac.zhd);// ���캯�������໥���ã�������this�����Ǻ�����������л�޴��Ρ�����
	}

	public Arc(Point cent, double r, double qds, double zds) {
		center = new Point(cent);
		radius = r;
		qhd = qds;
		zhd = zds;
		computertp(center, r, qds, zds);// ��������
		whzb();
	}

	public void computertp(Point cent, double rad, double qds, double zds) {// ��������
		if (zds < qds)
			zds = zds + Math.PI * 2;// ����յ����С���������������һ��2PI������Լ��������ʱ��Ӱ�졣
		double qx = cent.x + rad * Math.cos(qds);
		double qy = cent.y + rad * Math.sin(qds);
		double zx = cent.x + rad * Math.cos(zds);
		double zy = cent.y + rad * Math.sin(zds);
		// �����������
		double tx = 0.0, ty = 0.0;
		double tds = (qds + zds) / 2;
		double r = Math.sqrt((qx - cent.x) * (qx - cent.x) + (qy - cent.y) * (qy - cent.y));
		tx = r * Math.cos(tds) + cent.x;
		ty = r * Math.sin(tds) + cent.y;
		qd = new Point(qx, qy);
		td = new Point(tx, ty);
		zd = new Point(zx, zy);
	}

	public void whzb() {// ��������ά����������
		zb[0] = zd.x;
		zb[1] = zd.y;
		zb[2] = td.x;
		zb[3] = td.y;
		zb[4] = qd.x;
		zb[5] = qd.y;
		// zb[0]=qd.x;zb[1]=qd.y;zb[2]=td.x;zb[3]=td.y;zb[4]=zd.x;zb[5]=zd.y;
	}
}

class Circle extends tuys {
	Point center;
	double radius;

	// String name="Circle";
	public Circle(Point c, double r) {
		center = new Point(c);
		radius = r;
		// name="Circle";
	}

	public Circle(double cx, double cy, double r) {
		this(new Point(cx, cy), r);
	}

	public Circle(Circle sccl) {
		this(sccl.center, sccl.radius);
	}
}

class Solid extends tuys {
	Point yi, er, san, si;// ��һ�����������Ľǵ�(���£����£����ϣ�����)
	double zb[];

	public Solid(Point a, Point b, Point c, Point d) {
		yi = new Point(a);
		er = new Point(b);
		san = new Point(c);
		si = new Point(d);
		whzb();
	}

	public Solid(Solid sld) {
		yi = new Point(sld.yi);
		er = new Point(sld.er);
		san = new Point(sld.san);
		si = new Point(sld.si);
		whzb();
	}

	public void whzb() {// ���շ��������ܱ���д
		if (san.x == si.x && san.y == si.y) {// �жϵ�����͵��ĵ��Ƿ���ͬһ��
			zb = new double[6 + 2];
			zb[0] = yi.x;
			zb[1] = yi.y;
			zb[2] = er.x;
			zb[3] = er.y;
			zb[4] = san.x;
			zb[5] = san.y;
			zb[6] = yi.x;
			zb[7] = yi.y;
		} else {
			zb = new double[8 + 2];
			zb[0] = yi.x;
			zb[1] = yi.y;
			zb[2] = er.x;
			zb[3] = er.y;
			zb[4] = si.x;
			zb[5] = si.y;// ע��˳��
			zb[6] = san.x;
			zb[7] = san.y;
			zb[8] = yi.x;
			zb[9] = yi.y;
		}
	}
}

public class TUYUAN {
	// final public static double PI=3.1415926;
}
