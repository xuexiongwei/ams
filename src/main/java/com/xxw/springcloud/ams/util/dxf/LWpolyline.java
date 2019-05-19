package com.xxw.springcloud.ams.util.dxf;

public class LWpolyline extends tuys {
	Point num[];
	int plen;// ���������num����ĳ���
	double zb[];// ���xy��������飬�������Oracle���ݿ�
	// int close;//�Ƿ�պ�
	// String name="LWpolyline";

	public LWpolyline(int n, double xy[]) {// �����������x��y����
		// if(bh==1) n=n+1;//�պϵĻ�������ظ���һ����
		// close=bh;
		plen = n;
		num = new Point[n];
		zb = new double[n * 2];
		for (int i = 0; i < n * 2; ++i) {
			zb[i] = xy[i];
			// System.out.println(i);
			num[i / 2] = new Point();// num�����е�ÿ��Ԫ�ػ���Ҫnew Point ���� thanks for
										// ����LXK
			num[i / 2].x = xy[i];
			++i;
			zb[i] = xy[i];
			num[i / 2].y = xy[i];
		}
		name = "LWpolyline";
	}

	public LWpolyline(LWpolyline slw) {
		this(slw.plen, slw.zb);
	}

	public void whzb() {// ����Point����ͳ��ȣ�ά�����������
		for (int i = 0; i < plen; ++i) {
			zb[i * 2] = num[i].x;
			zb[i * 2 + 1] = num[i].y;
		}
	}

	public boolean isRect() {// �ж��Ƿ�Ϊ����
		if (plen != 4)
			return false;
		if (num[0].x == num[1].x && num[1].y == num[2].y && num[2].x == num[3].x && num[3].y == num[0].y
				&& num[0].y != num[1].y && num[1].x != num[2].x && num[2].y != num[3].y && num[3].x != num[0].x)
			return true;
		else if (num[0].y == num[1].y && num[1].x == num[2].x && num[2].y == num[3].y && num[3].x == num[0].x
				&& num[0].x != num[1].x && num[1].y != num[2].y && num[2].x != num[3].x && num[3].y != num[0].y)
			return true;
		else
			return false;
	}

	public Point[] getNum() {
		return num;
	}

	public void setNum(Point[] num) {
		this.num = num;
	}

	public int getPlen() {
		return plen;
	}

	public void setPlen(int plen) {
		this.plen = plen;
	}

	public double[] getZb() {
		return zb;
	}

	public void setZb(double[] zb) {
		this.zb = zb;
	}

}
