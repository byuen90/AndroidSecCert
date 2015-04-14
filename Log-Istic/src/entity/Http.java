package entity;

import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;

public class Http
{
	private String pid;
	private String dateTime;
	//request header
	private String destination;
	private String contentType;
	private int contentLength;
	private String contentEncoding;
	private String acceptEncoding;
	private String methodType;
	private int paramSize ;
	private String message;
	private String source;
	private String userAgent;
	//1 for malicious 2 for not malicious
	private int maliciousClass;
	private HashMap<String, Integer> specialAttributes;

	//Constructors
	public Http() {}
	public Http(String pid, String sourceApp, String dt, String dest, String contentType, int contentLength, 
			String cEnc, String aEnc, String methodType, int pSize, String msg, String userAgent, HashMap<String, Integer> specialAtt)
	{
		this.pid = pid;
		this.source = sourceApp;
		this.dateTime = dt;
		this.destination = dest;
		this.contentType = contentType;
		this.contentLength = contentLength;
		this.contentEncoding = cEnc;
		this.acceptEncoding = aEnc;
		this.methodType = methodType;
		this.paramSize = pSize;
		this.message = msg;
		this.userAgent = userAgent;
		this.specialAttributes = specialAtt;
	}
	
	//setter methods
	public void setPid(String pid)
	{
		this.pid = pid;
	}
	
	public void setDateTime(String dt)
	{
		this.dateTime = dt;
	}
	
	public void setDest(String dest)
	{
		this.destination = dest;
	}
	public void setMsg(String msg)
	{
		this.message = msg;
	}
	public void setSource(String src)
	{
		this.source = src;
	}
	public void setContentType(String cType)
	{
		this.contentType = cType;
	}
	public void setContentLength(int cLength)
	{
		this.contentLength = cLength;
	}
	public void setContentEncoding(String cEnc)
	{
		this.contentEncoding = cEnc;
	}
	public void setAcceptEncoding(String aEnc)
	{
		this.acceptEncoding = aEnc;
	}
	public void setMethodType(String mType)
	{
		this.methodType = mType;
	}	
	public void setParamSize(int pSize)
	{
		this.paramSize = pSize;
	}
	public void setMaliciousClass(int cls)
	{
		this.maliciousClass = cls;
	}
	public void setUserAgent(String uAgent)
	{
		this.userAgent = uAgent;
	}
	public void setSpecialAttribute(HashMap<String, Integer>  sAtt)
	{
		this.specialAttributes = sAtt;
	}
	
	//getter methods
	public String getPid()
	{
		return this.pid;
	}
	public String getDateTime()
	{
		return this.dateTime;
	}
	public String getMsg()
	{
		return this.message;
	}
	public String getDest()
	{
		return this.destination;
	}	
	public String getSource()
	{
		return this.source;
	}
	public String getContentType()
	{
		return this.contentType;
	}
	public String getContentLength()
	{
		return Integer.toString(this.contentLength);
	}
	public String getContentEncoding()
	{
		return this.contentEncoding;
	}
	public String getAcceptEncoding()
	{
		return this.acceptEncoding;
	}
	public String getMethodType()
	{
		return this.methodType;
	}	
	public String getParamSize()
	{
		return Integer.toString(this.paramSize);
	}
	public String getMaliciousClass()
	{
		return Integer.toString(this.maliciousClass);
	}
	public String getUserAgent()
	{
		return this.userAgent;
	}
	public HashMap<String, Integer> getSpecialAttribute()
	{
		return this.specialAttributes;
	}
}
