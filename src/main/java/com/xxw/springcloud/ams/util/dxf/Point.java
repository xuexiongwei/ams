package com.xxw.springcloud.ams.util.dxf;

public class Point extends tuys {
	double x, y;

	// String name="Point";
	public Point() {
		x = 0.0;
		y = 0.0;
		name = "Point";
	}

	public Point(Point a) {
		x = a.x;
		y = a.y;
		name = "Point";
	}

	public Point(double a, double b) {
		x = a;
		y = b;
		name = "Point";
	}

	public void shift(double sx, double sy, double xz, double cx, double cy) {// x��y�������ţ�����ԭ��ѡ��xz����,���ϲ���㣨��ԭ��任���λ�ã��ĺ�������
		x = x * sx; // ע��xz�ǻ���
		y = y * sy;
		double r = Math.sqrt(x * x + y * y);// �㣨x,y�������ԭ��ĳ���
		double yhd = 0;// �������ԭ���ԭʼ����
		if (x != 0 || y != 0)
			yhd = Math.acos(x / r);// ���벻��ԭ�㣬��r����0ʱ�����ܳ���
		if (y < 0)
			yhd = -1 * yhd;// ����a�Ⱥ�-a�ȵ�����ֵ��ͬ������Ҫͨ��y�����ж�һ�¡�
							// sin��cos��360���ڶ�����һһ��Ӧ�ġ�
		// System.out.print("x/r:");System.out.println(x/r);
		// System.out.print("yhd:");System.out.println(Math.toDegrees(yhd));
		// System.out.print("ԭʼ���ȣ�"); System.out.println(yhd);
		// System.out.print("x,y:"); System.out.println(x);
		// System.out.println(y);
		x = r * Math.cos(xz + yhd) + cx;// ����Ӧ����r�ˣ�������x��y������
		y = r * Math.sin(xz + yhd) + cy;
		// System.out.print("�޸ĺ�x,y:"); System.out.println(x);
		// System.out.println(y);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}