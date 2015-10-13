import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;

import com.teleinfo.vsp.client.transport.EppSession;
import com.teleinfo.vsp.client.transport.EppSessionImpl;
import com.teleinfo.vsp.epp.entity.EppAuthInfo;
import com.teleinfo.vsp.epp.entity.EppDnv;
import com.teleinfo.vsp.epp.entity.EppGreeting;
import com.teleinfo.vsp.epp.entity.EppResponseInput;
import com.teleinfo.vsp.epp.entity.EppRnv;
import com.teleinfo.vsp.epp.entity.EppVerificationCode;
import com.teleinfo.vsp.epp.entity.EppVspCreateSuccess;
import com.teleinfo.vsp.epp.entity.VSPDictType;
import com.teleinfo.vsp.epp.entity.EppRnvDocument;
import com.teleinfo.vsp.epp.response.EppResponse;
import com.teleinfo.vsp.epp.response.EppResponseDataCheckResult;
import com.teleinfo.vsp.epp.response.EppResponseDataCheckVsp;
import com.teleinfo.vsp.epp.response.EppResponseDataCreateVsp;
import com.teleinfo.vsp.epp.response.EppResponseDataInfoVsp;
import com.teleinfo.vsp.epp.util.EppParser;


public class VSPClient {
	
	private static EppSessionImpl sesImpl = new EppSessionImpl();
	private static EppSession ses;
	private static EppResponse response;
	private static EppParser parser;
	
	private static Random rand = new Random();
	
	private static String host = "ote.vsp.teleinfo.cn";
	private static int port = 3121;
	
	private static String regID = "teleinfo-client";
	private static String passWD = "foo-bar21";
	private static String newPW = "foo-bar21";
	
	private static String clintID = "cltr-" + rand.nextInt();
	
	private static String infopwd = "clientXX--" + rand.nextInt();
	
	private static String clintIDLogout = "logout-" + rand.nextInt();
	
	
	private  void getConnection() throws Exception {
		SSLContext ctx = SSLContext.getInstance("TLS");
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		KeyStore ks = KeyStore.getInstance("JKS");
		KeyStore ts = KeyStore.getInstance("JKS");
		ks.load(new FileInputStream("cert/client_cert/client.jks"), "changeit"
				.toCharArray());
		ts.load(new FileInputStream("cert/client_cert/clienttrust.jks"), "changeit"
				.toCharArray());
		kmf.init(ks, "changeit".toCharArray());
		tmf.init(ts);

		ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
		SSLEngine sslEngine = ctx.createSSLEngine();
	    sslEngine.setUseClientMode(true);
		sesImpl.setSslContext(ctx);

		EppGreeting greeting;
		greeting = sesImpl.connect(host, port);
		System.out.println(greeting);
		if (greeting == null) {
			throw new Exception("can't connect with server");
		}
	}

	private  void login() throws Exception {
		response = sesImpl.login(regID, passWD, clintID);
	}

	private  void logout() throws Exception {
		response = sesImpl.logout(clintIDLogout);
		sesImpl.close();
	}
	
	private  void hello() throws Exception {
		 sesImpl.hello();
	}
	
	private  void checkDomain() throws Exception {
		  	List<String> names=new ArrayList<String>();
		  	names.add("jiangzm.com");
		  	names.add("jiangzm1.com");
		  	names.add("xijp.com");
		  	names.add("xijp1.com");
		  	names.add("xn--55qw42g.com");
		  	EppResponse res = sesImpl.checkDomain(names, clintID);
		  	System.out.println("############checkDomain start ###################");
		  	EppResponseDataCheckVsp vspRes = (EppResponseDataCheckVsp) res.getResData();
		  	List<EppResponseDataCheckResult> checkList = vspRes.getCheckResults();
		  	for(EppResponseDataCheckResult result:checkList){
		  		System.out.println(result.toString());
		  	}
		  	System.out.println("############checkDomain end ###################");
	}

	private void createDomainNV() {
		EppResponse res = sesImpl.createDomainNV("test-vsp.com",infopwd.toString(),clintID);
		EppResponseDataCreateVsp vspRes =(EppResponseDataCreateVsp) res.getResData();
		System.out.println("############createDomainNV start ###################");
		if(vspRes.getSuccess()!=null){
			 EppVspCreateSuccess success = vspRes.getSuccess();
			 System.out.println("status:"+success.getStatus());
			 System.out.println("code:"+success.getCode());
			 EppVerificationCode signedcode = success.getVcode();
			 System.out.println("encodedSignedCode:"+signedcode.getSignedCode());
		 }
		System.out.println("############createDomainNV end ###################");
	}
	
	private void createRealNameNV(){
		 List<EppRnvDocument> docs = new ArrayList<EppRnvDocument>();
		 EppRnvDocument d1 = new EppRnvDocument();
		 d1.setFileContent(Base64Util.GetImageStr("test.jpg"));
		 d1.setFileType("jpg");
		 docs.add(d1);
		 EppResponse res = sesImpl.createRealNameNV(VSPDictType.RNV_ROLE_ORG, "凭祥市国土资源局", "007723543", VSPDictType.RNV_PROOF_TYPE_POE, docs,infopwd.toString(),clintID);
		 System.out.println("############createRealNameNV start ###################");
		 EppResponseDataCreateVsp vspRes =(EppResponseDataCreateVsp) res.getResData();
		 if(vspRes.getSuccess()!=null){
			 EppVspCreateSuccess success = vspRes.getSuccess();
			 System.out.println("status:"+success.getStatus());
			 System.out.println("code:"+success.getCode());
			 EppVerificationCode signedcode = success.getVcode();
			 System.out.println("encodedSignedCode:"+signedcode.getSignedCode());
		 }
		 System.out.println("############createRealNameNV end ###################");
	}
	
	private void infoNVBySignCode(){
		 EppResponse res = sesImpl.infoNVBySignCode("1-032067b5c041a71903e053e767a8c09381", clintID);
		 System.out.println("############infoNVBySignCode start ###################");
		 EppResponseDataInfoVsp resData1 = (EppResponseDataInfoVsp) res.getResData();
		 System.out.println(resData1.getSignCode().getStatus());
		 EppVerificationCode vc = resData1.getSignCode().getEncodeSignedCode();
		 System.out.println(vc.getSignedCode());
		 System.out.println("############infoNVBySignCode end ###################");
	}
	
	private void infoNVByInput(){
		EppResponse res =sesImpl.infoNVByInput("1-032067b5c041a71903e053e767a8c09381", clintID);
		System.out.println("############infoNVByInput start ###################");
		EppResponseDataInfoVsp resData1 = (EppResponseDataInfoVsp) res.getResData();
		EppResponseInput input = resData1.getInput();
		if(input.getDnv()!=null){
			EppDnv dnv = input.getDnv();
			System.out.println(dnv.toString());
		}else if(input.getRnv()!=null){
			EppRnv rnv = input.getRnv();
			System.out.println(rnv.toString());
		}
		if(resData1.getAuthInfo()!=null){
			EppAuthInfo  authInfo = resData1.getAuthInfo();
			System.out.println("authInfoPwd:"+authInfo.getPw());
		}
		System.out.println("############infoNVByInput end ###################");
	}
	
	private void upDateAuthInfo(){
		sesImpl.upDateAuthInfo("1-032067b5c041a71903e053e767a8c09381", infopwd.toString(), clintID);
	}
	
	private void pollRequest(){
		sesImpl.pollRequest(clintID);
	}
	
	private void pollAck(){
		sesImpl.pollAck("155", clintID);
	}
	
	public static void main(String[] args) throws Exception {
		VSPClient client = new VSPClient();
		client.getConnection();
		client.login();
		client.hello();
		client.checkDomain();
		client.createDomainNV();
		client.createRealNameNV();
		client.infoNVBySignCode();
		client.infoNVByInput();
		client.upDateAuthInfo();
		client.pollRequest();
		client.pollAck();
		client.logout();
	}


}
