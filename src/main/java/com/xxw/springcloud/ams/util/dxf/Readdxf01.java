package com.xxw.springcloud.ams.util.dxf;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class Readdxf01 {

	/**
	 * @param args
	 *            the command line arguments
	 */
	final public static String fileName = "F:\\xjbl.DXF";
	final public static String tableName = "cad";
	// final public static String fileName="��׼��_t3.dxf";
	// final public static String fileName="fromʳ�ý���ƽ��ͼ.dxf";
	// final public static String fileName="from���ݾƵ�ʮ��ƽ��ͼ.dxf";
	// final public static String fileName="ts����.dxf";

	public static int cnt = 0;
	final public static int zbsrid = 32774;
	// final public static double PI=3.1415926;
	public static List<List> lst = new ArrayList();// �洢ÿ�����ͼԪ��ɲ��֡� ע��static
	public static List<String> kname = new ArrayList();// lst��Ӧ�Ŀ�����
	public static List<String> filter = new ArrayList();// ��ȥ��ȡͼԪ��ͼ����
	public static List<String> mentc = new ArrayList();// ������ͼ��
	public static String wintcnm = "WINDOW";// �Ŵ���ͼ����
	public static List allty = new ArrayList();// �洢���е�ͼԪ
	public static List door = new ArrayList();// �����ű任������ֱ�߶�

	public static double huDu(Point a, Point c) {// ��cΪԲ�ģ���a�����c�Ļ��ȡ�
		double r = Math.sqrt((a.x - c.x) * (a.x - c.x) + (a.y - c.y) * (a.y - c.y));// �뾶��
		double hd = 0;
		if (a.x != c.x || a.y != c.y)
			hd = Math.acos((a.x - c.x) / r);// ���벻��Բ�ģ���r����0ʱ�����ܳ���
		if (a.y < c.y)
			hd = -1 * hd;// ����a��Բ��c�·��������Ϊ���ġ�
		return hd;
	}

	public static Solid readSolid(BufferedReader bfr) throws IOException {
		double x1 = 0, x2 = 0, y1 = 0, y2 = 0, z1 = 0, z2 = 0, x3 = 0, y3 = 0, z3 = 0, x4 = 0, y4 = 0, z4 = 0;
		String s1 = null, s2 = null;
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals("  8") == false)
			; // ͼ��
		String tc = new String(s2);
		// ��ȡ������
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 10") == false)
			; // x����
		x1 = Double.parseDouble(s2);
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 20") == false)
			;
		y1 = Double.valueOf(s2).doubleValue();
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 30") == false)
			;
		z1 = Double.valueOf(s2).doubleValue();

		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 11") == false)
			;
		x2 = Double.valueOf(s2).doubleValue();
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 21") == false)
			;
		y2 = Double.valueOf(s2).doubleValue();
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 31") == false)
			;
		z2 = Double.valueOf(s2).doubleValue();

		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 12") == false)
			;
		x3 = Double.valueOf(s2).doubleValue();
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 22") == false)
			;
		y3 = Double.valueOf(s2).doubleValue();
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 32") == false)
			;
		z3 = Double.valueOf(s2).doubleValue();

		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 13") == false)
			;
		x4 = Double.valueOf(s2).doubleValue();
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 23") == false)
			;
		y4 = Double.valueOf(s2).doubleValue();
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 33") == false)
			;
		z4 = Double.valueOf(s2).doubleValue();

		if (filter.contains(tc))
			return null;
		Solid Sld = new Solid(new Point(x1, y1), new Point(x2, y2), new Point(x3, y3), new Point(x4, y4));
		return Sld;
	}

	public static Arc readArc(BufferedReader bfr)
			throws IOException, InstantiationException, IllegalAccessException, SQLException {
		double cx = 0.0, cy = 0.0, rad = 0.0, qd = 0.0, zd = 0.0;
		String s1 = null, s2 = null;
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals("  8") == false)
			; // ͼ��
		String tc = new String(s2);
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 10") == false)
			;
		cx = Double.parseDouble(s2);
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 20") == false)
			;
		cy = Double.parseDouble(s2);
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 40") == false)
			; // �뾶
		rad = Double.parseDouble(s2);
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 50") == false)
			; // ��㻡��
		qd = Double.parseDouble(s2) / 180 * Math.PI;
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 51") == false)
			; // �յ㻡��
		zd = Double.parseDouble(s2) / 180 * Math.PI;

		/*
		 * if(zd<qd)
		 * zd=zd+Math.PI*2;//����յ����С���������������һ��2PI������Լ��������ʱ��Ӱ�졣 double
		 * qx=cx+rad*Math.cos(qd); double qy=cy+rad*Math.sin(qd); double
		 * zx=cx+rad*Math.cos(zd); double zy=cy+rad*Math.sin(zd); //����������� double
		 * tx=0.0,ty=0.0; if(qx!=zx){ tx=(qx+zx)/2;
		 * ty=Math.sqrt(rad*rad-(cx-tx)*(cx-tx))+cy; //!!!�����˼�cy
		 * //��������⣬tyֻ����û�и� } else{ ty=(qy+zy)/2; tx=cx; } //double qsita=huDu(new
		 * Point(qx,qy),new Point(cx,cy)); //double zsita=huDu(new Point(zx,zy),new
		 * Point(cx,cy)); double td=(qd+zd)/2; double
		 * r=Math.sqrt((qx-cx)*(qx-cx)+(qy-cy)*(qy-cy)); tx=r*Math.cos(td)+cx;
		 * ty=r*Math.sin(td)+cy;
		 */

		if (filter.contains(tc))
			return null;
		Arc arc = new Arc(new Point(cx, cy), rad, qd, zd);
		return arc;
	}

	public static Circle readCircle(BufferedReader bfr)
			throws IOException, InstantiationException, IllegalAccessException, SQLException {
		// Բ�����ģ�������˲������⡣���ܿ����û�������Բ����֪�����������ܲ������غ�
		String s1 = null, s2 = null;
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals("  8") == false)
			; // ͼ��
		String tc = new String(s2);
		double center_x = 0.0, center_y = 0.0, radius = 0.0;
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 10") == false)
			; // Բ��
		center_x = Double.parseDouble(s2);
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 20") == false)
			;
		center_y = Double.parseDouble(s2);
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 40") == false)
			; // ����z�����0ֵ�����뾶
		radius = Double.parseDouble(s2);

		if (filter.contains(tc))
			return null;
		Circle ccle = new Circle(center_x, center_y, radius);
		return ccle;
		/*
		 * JGeometry geo=JGeometry.createCircle(center_x,center_y,radius,zbsrid);
		 * connect_database(geo);
		 */
	}

	public static Line readLine(BufferedReader bfr)
			throws InstantiationException, IllegalAccessException, SQLException, IOException {
		// ������������ǰ���xyz��˳������ģ���Ҫ��˳���޹أ����԰��������ĳ���whileѭ����һֱ����Ȼ��ѭ�������жϡ�
		// System.out.println("heihei");
		double x1 = 0, x2 = 0, y1 = 0, y2 = 0, z1 = 0, z2 = 0;
		String s1 = null, s2 = null;
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals("  8") == false)
			; // ͼ��
		String tc = new String(s2);
		// ��ȡ������
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 10") == false)
			; // x����
		// System.out.println("s1:"+s1+" s2:"+s2);
		x1 = Double.parseDouble(s2);
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 20") == false)
			;
		y1 = Double.valueOf(s2).doubleValue();
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 30") == false)
			;
		z1 = Double.valueOf(s2).doubleValue();

		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 11") == false)
			; // x����
		x2 = Double.valueOf(s2).doubleValue();
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 21") == false)
			;
		y2 = Double.valueOf(s2).doubleValue();
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 31") == false)
			;
		z2 = Double.valueOf(s2).doubleValue();

		if (filter.contains(tc))
			return null;
		Line l1 = new Line(x1, y1, x2, y2);
		return l1;
		// System.out.println(x1+" "+y1+" "+x2+" "+y2);
		// int elemInfo[]={1,2,1};
		/*
		 * double ordinates[]={x1,y1,x2,y2}; JGeometry
		 * geo=JGeometry.createLinearLineString(ordinates,2,32774);
		 * connect_database(geo);
		 */
	}

	public static LWpolyline readLWpolyline(BufferedReader bfr)
			throws IOException, InstantiationException, IllegalAccessException, SQLException {
		double x = 0, y = 0;
		String s1 = null, s2 = null;
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals("  8") == false)
			; // ͼ��
		String tc = new String(s2);
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 90") == false)
			; // ������
		int num = Integer.parseInt(s2.trim());
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 70") == false)
			; // �պ���
		int cls = Integer.parseInt(s2.trim()); // �²ۣ�����ӵ������Լ�ͨ����ͼ�ԱȲ�֪�������־������Ƿ�պϰ��������ĵ��ﾹȻд�رգ����Ǳպϡ�������
		if (cls == 1)
			num = num + 1;// �պϵĻ�������ظ���һ����
		double ordinates[] = new double[num * 2];
		for (int i = 0; i < (cls == 1 ? num - 1 : num); ++i) {// �м�ı��ʽ������iС��ԭʼ��num
			while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 10") == false)
				; // ����
			x = Double.parseDouble(s2);
			while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 20") == false)
				; // ����
			y = Double.parseDouble(s2);
			ordinates[i * 2] = x;
			ordinates[i * 2 + 1] = y;
		}
		if (cls == 1) {
			ordinates[2 * num - 2] = ordinates[0];
			ordinates[2 * num - 1] = ordinates[1];
		} // �ظ���һ��
		if (filter.contains(tc))
			return null;
		LWpolyline lwln = new LWpolyline(num, ordinates);
		return lwln;
		/*
		 * for(int i=0;i<2*num;++i){ System.out.println(ordinates[i]+" "); }
		 * 
		 * JGeometry geo=JGeometry.createLinearLineString(ordinates,2,32774);
		 * connect_database(geo);
		 */
	}

	public static int find(String name) {
		int i = 0;
		int flag = 0;// �Ƿ��ҵ�
		for (; i < kname.size(); ++i) {
			if (kname.get(i).equals(name)) {
				flag = 1;
				break;
			}
		}
		if (flag == 1)
			return i;
		else
			return -1;
	}

	/**/public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(src);
		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(byteIn);
		@SuppressWarnings("unchecked")
		List<T> dest = (List<T>) in.readObject();
		return dest;
	}

	public static void readInsert(BufferedReader bfr)
			throws IOException, InstantiationException, IllegalAccessException, SQLException, ClassNotFoundException {
		String s1 = null, s2 = null;
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals("  8") == false)
			; // ͼ��
		String tc = new String(s2);
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals("  2") == false)
			; // ������
		String name = s2;
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 10") == false)
			; // �����xֵ
		double cx = Double.parseDouble(s2);
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 20") == false)
			; // �����yֵ
		double cy = Double.parseDouble(s2);
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null && s1.equals(" 30") == false)
			; // �����zֵ
		double cz = Double.parseDouble(s2);
		// System.out.println("hehehe");
		double sx = 1, sy = 1, sz = 1, xz = 0;// xyz�����ű�������ת�Ƕȣ����ȣ�
		bfr.mark(100);// ����Ҳ��Ҫmark�£���Ȼ����30��ֱ�ӽ�����������һͼԪ������0�������ͻ��˵���һ�����������whileѭ��mark�ĵط��ˡ���
						// �������˺ó�ʱ�䣬���ż����е�txt��������֪��ʲô��bug����
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null) {
			if (s1.equals(" 41"))
				sx = Double.parseDouble(s2);
			else if (s1.equals(" 42"))
				sy = Double.parseDouble(s2);
			else if (s1.equals(" 43"))
				sz = Double.parseDouble(s2);
			else if (s1.equals(" 50"))
				xz = Math.toRadians(Double.parseDouble(s2)); // xz=Double.parseDouble(s2)/180*Math.PI;//shift�������õ���cos������Ϊ���ȣ���������ת��Ϊ����
			else if (s1.equals("  0")) {// ������˵��ж�����ֻ����һ��ͼԪ��ʼ����β��������Ϊ0
				bfr.reset();// ���˵���һ��mark��λ�ã����������С����˵�ԭ���ǿ�����Щ��ѡ���Ծ�û�У�Ȼ���������һ��ͼԪ�Ŀ�ͷ���߿�Ľ�β��
				break;
			} else
				break;// ����û�д���70��71�����췽��ȵ������
			bfr.mark(100);// �����������ǰλ�á�����Ϊ���������ݣ��㹻�󼴿ɣ��������ѡ��100
		}

		if (filter.contains(tc))
			return;// ��ͼ����Ҫ���˵�ͼ�����У���ֱ�ӽ��������ٴ洢��
		System.out.print("kname:");
		System.out.println(name);
		int indx = find(name);
		System.out.print("indx:");
		System.out.println(indx);
		if (indx < 0)
			return;
		// if(name.compareTo("$DorLib2D$00000001")!=0) return;//��ʱ�ȴ�����1��һ�������
		List tlst = new ArrayList(lst.get(indx));// List tlst=deepCopy(lst.get(indx));
		// List JGlst=new
		// ArrayList();//����һ���տ��ͼԪ������������ʱ��û�õ������ܷ�������޸�ʶ���ŵĵط�
		List<Arc> AClst = new ArrayList();// ��鶨���еĻ�

		for (int i = 0; i < tlst.size(); ++i) {
			// ����lst��indxλ��Ԫ��
			/**/if (tlst.get(i).getClass() == Line.class) {// ֱ��
				Line templ = new Line((Line) tlst.get(i));// ���Ƕ�List��Ԫ�ؽ���ǿ������ת����Ȼ���ٴ���һ������
				templ.qd.shift(sx, sy, xz, cx, cy);
				templ.zd.shift(sx, sy, xz, cx, cy);
				templ.whzb(); // �²ۣ���Ҫ���ˣ�Ϊʲô����ļ�����֪��ά�����꣬�������ˡ�����
				// ����Oracle
				// JGeometry geo=JGeometry.createLinearLineString(templ.zb,2,32774);
				// connect_database(geo);
			} else if (tlst.get(i).getClass() == LWpolyline.class) {// �����
				LWpolyline templw = new LWpolyline((LWpolyline) tlst.get(i));
				for (int j = 0; j < templw.plen; ++j) {
					templw.num[j].shift(sx, sy, xz, cx, cy);
				}
				templw.whzb();
				// ����Oracle
				if (templw.isRect()) {

				}
				// JGeometry geo=null;//if(templw.close==1)
				// geo=JGeometry.createLinearPolygon(templw.zb, 2,
				// zbsrid);//���������������������Σ���Ϊ������ڲ�������
				// geo=JGeometry.createLinearLineString(templw.zb,2,zbsrid);
				// connect_database(geo);
			} else if (tlst.get(i).getClass() == Arc.class) {// ��
				Arc tempac = new Arc((Arc) tlst.get(i));
				tempac.qd.shift(sx, sy, xz, cx, cy);
				tempac.td.shift(sx, sy, xz, cx, cy);
				tempac.zd.shift(sx, sy, xz, cx, cy);
				tempac.center.shift(sx, sy, xz, cx, cy);
				tempac.whzb();
				// ����Oracle
				// JGeometry geo=JGeometry.createArc2d(tempac.zb, 2, zbsrid);
				// System.out.print("����㣺"); System.out.println(cx);System.out.println(cy);
				// System.out.print("Arc:");
				// System.out.println(tempac.qd.x);System.out.println(tempac.qd.y);System.out.println(tempac.td.x);System.out.println(tempac.td.y);System.out.println(tempac.zd.x);System.out.println(tempac.zd.y);
				// connect_database(geo);
				AClst.add(tempac);
			} /**//**/
			else if (tlst.get(i).getClass() == Circle.class) {// Բ //������ڰ뾶����ı������������⣬Ҳ����Բ���������任Ϊ���Щ��
				Circle tempccl = new Circle((Circle) tlst.get(i));
				tempccl.center.shift(sx, sy, xz, cx, cy);
				tempccl.radius = tempccl.radius * sx;// �������궼����sx�����뾶����sx����
														// �뾶����ı����Ȳ��ǲ��䣬Ҳ���Ǻ�����������ı���֮��
				// ����Oracle
				// JGeometry
				// geo=JGeometry.createCircle(tempccl.center.x,tempccl.center.y,tempccl.radius,zbsrid);
				// System.out.println("Circle:"); System.out.println(tempccl.center.x);
				// System.out.println(tempccl.center.y); System.out.println(tempccl.radius);
				// connect_database(geo);
			} else if (tlst.get(i).getClass() == Solid.class) {
				Solid tempsld = new Solid((Solid) tlst.get(i));
				tempsld.yi.shift(sx, sy, xz, cx, cy);
				tempsld.er.shift(sx, sy, xz, cx, cy);
				tempsld.san.shift(sx, sy, xz, cx, cy);
				tempsld.si.shift(sx, sy, xz, cx, cy);
				tempsld.whzb();
				// JGeometry geo=JGeometry.createLinearLineString(tempsld.zb,2,zbsrid);
				// connect_database(geo);
			}
		} // end of ����for
		if (tc.compareTo(wintcnm) == 0) {// ���Ŵ�ͼ��
			if (AClst.size() == 1) {
				Arc tarc = AClst.get(0);
				// if(tarc.)
			}
		}
		// return JGlst;
	}

	public static List<LWpolyline> readEntities(BufferedReader bfr)
			throws IOException, InstantiationException, IllegalAccessException, SQLException, ClassNotFoundException {

		List<LWpolyline> pointList = new ArrayList<LWpolyline>();

		String s1 = null, s2 = null;
		int flag = 0;// ָʾ�����Ƿ�ΪENTITIES�Σ�1Ϊ�ǣ�0Ϊ��
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null) { // && s2.equals("ENDSEC")==false){

			if (s1.equals("  0") && s2.equals("SECTION")) {
				s1 = bfr.readLine();
				s2 = bfr.readLine();
				if (s1.equals("  2") && s2.equals("ENTITIES")) {
					flag = 1;
					System.out.println("begin in Entities");
					continue;
				} // ��ʼ��ENTITIES��
			}
			if (flag == 0)
				continue; // �������ݲ���ENTITIES��
			if (flag == 1 && s1.equals("  0") && s2.equals("ENDSEC")) {
				flag = 0;
				System.out.println("end of Entities");
				break;
			} // ����ENTITIES��
			if (s1.equals("  0")) {// ���ж�s1ֱ���ж�s2�ǲ��Եġ�
				if (s2.equals("LINE")) {// ֱ��
					Line ln = readLine(bfr);
					if (ln != null) {
						System.out.println("Line:" + ln);
						// JGeometry geo=JGeometry.createLinearLineString(ln.zb,2,zbsrid);
						// connect_database(geo);
						// allty.add(geo);
					}
				} else if (s2.equals("LWPOLYLINE")) {// �����
					LWpolyline lwln = readLWpolyline(bfr);
					if (lwln != null) {

						pointList.add(lwln);
						// System.out.println("------------------------------------------");
						// Point[] ps = lwln.num;
						// for (Point point : ps) {
						// System.out.println("point:"+point.x+","+point.y);
						// }

						// JGeometry geo=JGeometry.createLinearLineString(lwln.zb,2,zbsrid);
						// connect_database(geo);
						// allty.add(geo);
					}
				} else if (s2.equals("CIRCLE")) {// Բ
					Circle ccle = readCircle(bfr);
					if (ccle != null) {
						System.out.println("Circle:" + ccle);
						// JGeometry
						// geo=JGeometry.createCircle(ccle.center.x,ccle.center.y,ccle.radius,zbsrid);
						// connect_database(geo);
						// allty.add(geo);
					}
				} else if (s2.equals("ARC")) {// ��
					Arc arc = readArc(bfr);
					if (arc != null) {
						System.out.println("Arc:" + arc);
						// JGeometry geo=JGeometry.createArc2d(arc.zb, 2, zbsrid); //new
						// JGeometry(2,zbsrid,elemI,ordinates);
						// connect_database(geo);
						// allty.add(geo);
					}
				} /**/
				else if (s2.equals("INSERT")) {// ���տ�ͼԪ
					readInsert(bfr);
					/*
					 * List<JGeometry> Jlst=readInsert(bfr); for(int i=0;i<Jlst.size();++i){
					 * connect_database(Jlst.get(i)); }
					 */
				} else if (s2.equals("SOLID")) {
					Solid sld = readSolid(bfr);
					if (sld != null) {
						System.out.println("Solid:" + sld);
						// JGeometry geo=JGeometry.createLinearLineString(sld.zb,2,zbsrid);
						// connect_database(geo);
					}
				}
			} // if-s1
		}

		return pointList;
	}

	public static void readBlocks(BufferedReader bfr)
			throws IOException, InstantiationException, IllegalAccessException, SQLException {
		String s1 = null, s2 = null;
		int flag = 0;// ָʾ�����Ƿ�ΪBLOCKS�Σ�1Ϊ�ǣ�0Ϊ��
		while ((s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null) {
			if (s1.equals("  0") && s2.equals("SECTION")) {
				s1 = bfr.readLine();
				s2 = bfr.readLine();
				// System.out.println("s1:"+s1+" s2:"+s2);
				if (s1.equals("  2") && s2.equals("BLOCKS")) {
					flag = 1;
					System.out.println("begin in Blocks");
					continue;
				} // ��ʼ��BLOCKS��
			}
			if (flag == 0)
				continue; // �������ݲ���BLOCKS��
			if (flag == 1 && s1.equals("  0") && s2.equals("ENDSEC")) {
				flag = 0;
				System.out.println("end of Blocks");
				break;
			} // ����BLOCKS��
			if (s1.equals("  0") && s2.equals("BLOCK")) {// ÿ������Ŀ
				int kflg = 0;// ����Ŀ�Ƿ����
				List tl = new ArrayList();
				while (s1.compareTo("  2") != 0) {
					s1 = bfr.readLine();
					s2 = bfr.readLine();
				}
				kname.add(s2);// ���������
				while (kflg == 0 && (s1 = bfr.readLine()) != null && (s2 = bfr.readLine()) != null) {
					if (s1.equals("100") && s2.equals("AcDbBlockEnd")) {// �ÿ���Ŀ����
						kflg = 1;
						lst.add(tl);
					}
					if (s1.equals("  0")) {
						if (s2.equals("LINE")) {
							Line ln = readLine(bfr);
							if (ln != null)
								tl.add(ln);
						} else if (s2.equals("LWPOLYLINE")) {
							LWpolyline lwln = readLWpolyline(bfr);
							if (lwln != null)
								tl.add(lwln);
						} else if (s2.equals("ARC")) {
							Arc arc = readArc(bfr);
							if (arc != null)
								tl.add(arc);
						} else if (s2.equals("CIRCLE")) {
							Circle ccle = readCircle(bfr);
							if (ccle != null)
								tl.add(ccle);
						} else if (s2.equals("INSERT")) {// Ƕ�׿鶨��

						} else if (s2.equals("SOLID")) {
							Solid sld = readSolid(bfr);
							if (bfr != null)
								tl.add(sld);
						}
					}
				}
			}
		}

		System.out.println("kname:" + kname + ",lst=" + lst);
	}

	public static void createindex() throws InstantiationException, IllegalAccessException, SQLException {
		// �������ݿ�����
		String Driver = "oracle.jdbc.driver.OracleDriver"; // �������ݿ�ķ���
		String URL = "jdbc:oracle:thin:@127.0.0.1:1521:indoor"; // indoorΪ���ݿ��SID
		String Username = "indooradmin"; // �û���
		String Password = "indoor"; // ����
		// String tableName="cad";
		String colName = "geom";
		try {
			Class.forName(Driver).newInstance(); // �������ݿ�����
			Connection con = DriverManager.getConnection(URL, Username, Password);
			if (!con.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			Statement stmt = con.createStatement();

			String sql = "delete from user_sdo_geom_metadata";
			System.out.println("Executing query:'" + sql + "'");
			// PreparedStatement stmt=con.prepareStatement(sqlInsert);
			stmt.executeUpdate(sql);

			sql = "insert into user_sdo_geom_metadata values('" + tableName + "','" + colName
					+ "',sdo_dim_array(sdo_dim_element('x',0,10000,0.5),sdo_dim_element('y',0,10000,0.5)),32774)";
			System.out.println("Executing query:'" + sql + "'");
			stmt.executeUpdate(sql);

			sql = "delete from user_sdo_index_metadata";
			System.out.println("Executing query:'" + sql + "'");
			stmt.executeUpdate(sql);

			sql = "drop index cad_index";
			System.out.println("Executing query:'" + sql + "'");
			stmt.executeUpdate(sql);

			sql = "create index cad_index on cad(geom) indextype is mdsys.spatial_index";
			System.out.println("Executing query:'" + sql + "'");
			stmt.executeUpdate(sql);

			stmt.close();
			con.close();
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(Readdxf01.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
		// TODO code application logic here
		File file = new File(fileName);
		FileReader fr = new FileReader(file);
		BufferedReader bfr = new BufferedReader(fr);
		System.out.println("here!");
		String bz = new String("AXIS");// ��ע�ߣ�����������ע��Բ�Լ���������ֱ�ߣ�
		String zx = new String("DOTE");// ��ɫ����
		String bk = new String("�Ӵ���");// ����ߵı߿����
		String tk = new String("ͼ��");// ���½ǵ�ͼ��Ϣ��������Ϣ�߿�
		// Scanner reader=new Scanner(System.in);
		// //StringBuffer temps=new StringBuffer();
		// String temps=null;
		// //String test="hello"; test=temps;
		// System.out.print("�����ע��ͼ�㣺");
		// if((temps=reader.nextLine()).trim().compareTo("")!=0) bz=new String(temps);
		// filter.add(bz);
		// System.out.print("��������ͼ�㣺");
		// if((temps=reader.nextLine()).trim().compareTo("")!=0) zx=new String(temps);
		// filter.add(zx);
		// System.out.print("����߿���ͼ�㣺");
		// if((temps=reader.nextLine()).trim().compareTo("")!=0) bk=new String(temps);
		// filter.add(bk);
		// System.out.print("�������½���Ϣ�߿�ͼ�㣺");
		// if((temps=reader.nextLine()).trim().compareTo("")!=0) tk=new String(temps);
		// filter.add(tk);
		// while(true){
		// System.out.print("��������ͼ������(q)��");
		// temps=reader.nextLine();
		// if(temps.compareTo("q")==0) break;
		// if(temps.trim().compareTo("")!=0) filter.add(temps);
		// }
		// //������ȡ������ͼ����
		// while(true){
		// System.out.print("����������ͼ������(q)��");
		// temps=reader.nextLine();
		// if(temps.compareTo("q")==0) break;
		// if(temps.trim().compareTo("")!=0) mentc.add(temps);
		// }

		// readBlocks(bfr);
		readEntities(bfr);
		// readCircle(bfr);

		// createindex(); //����������
		// �о����ǿ��Է���SQL�ļ����Ϊ�������ݿ����Ҫִ��SQL�ļ��ġ�������ִ�У�������������ڣ�drop
		// index��ͻ��쳣��
		System.out.println("There!");
		bfr.close();
		fr.close();
	}
}

/*
 * public static void test_rdBlocks() throws IOException{ File file=new
 * File("test_rdBlocks.txt"); FileWriter fr=new FileWriter(file); BufferedWriter
 * bfw= new BufferedWriter(fr); for(int i=0;i<kname.size();++i){
 * bfw.write("kuai-name:");//bfw.newLine(); bfw.write(kname.get(i));
 * bfw.newLine(); for(int j=0;j<lst.get(i).size();++j){
 * if(lst.get(i).getClass()==Line.class){ Line templn=(Line)lst.get(i).get(j);
 * bfw.write("10"); bfw.newLine(); bfw.write(""+templn.qd.x); bfw.newLine(); } }
 * } }
 */