package chatconnect;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.ImageIcon;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Chat {
	/**
	 * 发送消息并获得回复
	 * 
	 * @param question
	 *            待发送的消息
	 * @return answer 查询到的回答
	 */
	public String getRespond(String question) throws ParseException, IOException {
		// 1.创建httpClient实例
		CloseableHttpClient httpclient = HttpClients.createDefault();

		// 2.构建get方法实例
		String serverAddr = "http://www.tuling123.com/openapi/api";
		String INFO = URLEncoder.encode(question, "utf-8");
		String params = "b7bba818fd414f5d844407d250e9416c";
		String userid = "195260";
		HttpGet httpget = new HttpGet(serverAddr + "?key=" + params + "&info=" + INFO + "&userid=" + userid);

		// 3.执行方法，获得响应
		String answer = "null";
		try {
			CloseableHttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			// 4.处理得到的http实体
			String responseStr = EntityUtils.toString(entity);// 以String形式获取内容
			// System.out.println("接受到的原数据：" + responseStr);
			String tempstr = responseStr;
			// 5.处理接受到的数据
			JsonObject jsonObject = new JsonParser().parse(tempstr).getAsJsonObject();
			String code = jsonObject.get("code").getAsString();
			int tempnum;

			switch (code) {
			case "100000":
				answer = jsonObject.get("text").getAsString();
				break;
			case "200000":
				answer = jsonObject.get("text").getAsString() + "。" + jsonObject.get("url").getAsString();
				break;
			case "302000":
				JsonArray newsjsonObj = jsonObject.get("list").getAsJsonArray();
				answer = jsonObject.get("text").getAsString() + "\\n";
				int count_news_pic = 0;
				if (newsjsonObj.size() > 8)
					tempnum = 8;
				else
					tempnum = newsjsonObj.size();
				for (int i = 0; i < tempnum; i++) {
					JsonObject tempnob = newsjsonObj.get(i).getAsJsonObject();
					answer += "\\n" + " \\n" + (i + 1) + "、标题：" + tempnob.get("article").getAsString() + "\\n" + "图片："
							+ tempnob.get("icon").getAsString() + "pend" + "消息来源：" + tempnob.get("source").getAsString()
							+ "\\n" + "详细链接：" + tempnob.get("detailurl").getAsString();
					count_news_pic++;
					// System.out.println("answer");
				}
				for (int i = 0; i < count_news_pic; i++) {
					answer += "\\n            " + "\\n              " + "\\n              ";
				}
				break;
			case "308000":
				JsonArray recipejsonObj = jsonObject.get("list").getAsJsonArray();
				answer = jsonObject.get("text").getAsString() + "\\n";
				int count_cp_pic = 0;
				if (recipejsonObj.size() > 8)
					tempnum = 8;
				else
					tempnum = recipejsonObj.size();
				for (int i = 0; i < tempnum; i++) {
					JsonObject tempnob = recipejsonObj.get(i).getAsJsonObject();
					answer += "\\n" + " \\n" + (i + 1) + "、菜名：" + tempnob.get("name").getAsString() + "\\n" + "图片："
							+ tempnob.get("icon").getAsString() + "pend" + "材料:" + tempnob.get("info").getAsString()
							+ "\\n" + "详细链接：" + tempnob.get("detailurl").getAsString();
					count_cp_pic++;
				}
				for (int i = 0; i < count_cp_pic; i++) {
					answer += "\\n            " + "\\n              " + "\\n              " + "\\n              "
							+ "\\n              " + "\\n            " + "\\n              ";
				}
				break;
			}
		} catch (IOException e) {
			// 4.没网时自动调用本地数据库
			Localrobot localro = new Localrobot();
			answer = localro.localdriver(question);
		}
		return answer;
	}

	public Image getRespondPic(String picurl) throws ParseException, IOException {
		URL url = new URL(picurl);
		// 3.执行方法，获得响应
		Image anspic;
		try {
			BufferedInputStream bis = new BufferedInputStream(url.openStream());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int i;
			while ((i = bis.read()) != -1) {
				baos.write(i);
			}
			anspic = (new ImageIcon(baos.toByteArray())).getImage();
		} catch (IOException e) {
			// 4.没网时自动调用本地数据库
			anspic = (new ImageIcon("images/4.jpg").getImage());
		}
		return anspic;
	}
}
