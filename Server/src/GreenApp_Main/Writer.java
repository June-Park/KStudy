package GreenApp_Main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

public class Writer {

	private DataOutputStream data_Out;
	private DataInputStream data_in;
	private String str;

	// �깮�꽦�옄
	public Writer(DataOutputStream data_Out, DataInputStream data_In) {
		this.data_Out = data_Out;
		this.data_in = data_In;
		str = "";
	}

	// 湲곗〈�뿉 db, data�뒗 媛앹껜媛� �젣�꽕由�. �꽌踰� �뒪�젅�뱶�뿉�꽌 媛앹껜�쓽 媛� �젙蹂대�� array濡�
	// �씠�룞, array瑜� add�븳 �썑 蹂대궦�떎.
	// Vector<String>�씠 留ㅺ컻蹂��닔濡� �솕�쓣 �븣 湲��뵪濡� �쟾�넚
	public void send(Vector<String[]> vec) {
		for (int i = 0; i < vec.size(); i++) {
			for (int a = 0; a < vec.get(i).length; a++) {
				str += vec.get(i)[a];
				str += "//";
			}
			str += "////";
		}

		try {
			data_Out.writeUTF(str);
			data_Out.flush();

			this.str = "";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// String 諛곗뿴�씠 留ㅺ컻蹂��닔濡� �솕�쓣 �븣 Client濡� 蹂대궡�뒗 留ㅼ냼�뱶
	public void send(String[] str) {
		for (int i = 0; i < str.length; i++) {
			this.str += str[i];

			if (i != str.length) {
				this.str += "//";
			}
		}

		try {
			data_Out.writeUTF(this.str);
			data_Out.flush();
			this.str = "";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// int 諛곗뿴�씠 留ㅺ컻蹂��닔濡� �솕�쓣 �븣 Client濡� 蹂대궡�뒗 留ㅼ냼�뱶
	public void send(int[] str) {
		for (int i = 0; i < str.length; i++) {
			this.str += str[i];

			if (i != str.length) {
				this.str += "//";
			}
		}

		try {
			data_Out.writeUTF(this.str);
			data_Out.flush();

			this.str = "";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// String 諛붾줈 �뵆�윭�돩
	public void send(String str) {

		try {
			data_Out.writeUTF(str);
			data_Out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(int str) {

		try {
			data_Out.writeInt(str);
			data_Out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// �닔�뻾�븳 �씪�씠 �옒 �맟�뒗吏� �뙋�떒�빐�꽌 Y/N 蹂대궡�뒗 留ㅼ냼�뱶
	public void send(boolean bool) {
		try {
			if (bool) {
				data_Out.writeUTF("Y");
			} else {
				data_Out.writeUTF("N");
			}
			data_Out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}