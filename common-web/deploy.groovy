import org.oh.common.util.FileUtil;
import org.oh.common.util.FTPUtil;
import org.oh.common.util.TelnetUtil;

String os_name = "Microsoft";
String system_name = "skoh";
String source_dir = "target";
String target_dir = "target";
String source_file = project.properties["artifactId"] + "-" + project.properties["version"] + ".war";

String server_ip = "192.168.3.115";
int server_port = -1;
String user_id = "skoh";
String user_pw = "skoh";
String terminalType = null;
String charsetName = null;

String title = "Deploy \"" + server_ip + "\" server \"" + system_name + "\" system";

System.out.println("---------- " + title + " ----------");
System.out.println("Sending the war file to \"" + server_ip + "\"");
FTPUtil ftp = new FTPUtil(server_ip, server_port, user_id, user_pw);
ftp.backup(target_dir, source_file);
ftp.upload(source_dir, source_file, target_dir);
ftp.disconnect();

System.out.println("Restarting the WAS container \"" + system_name + "\"");
TelnetUtil telnet = new TelnetUtil(server_ip, server_port, user_id, user_pw, os_name, terminalType,
		charsetName);
telnet.excuteCommand("cd \\was\\" + system_name + "\\bin");
telnet.excuteCommand("shutdown");

telnet.excuteCommand("cd " + target_dir);
telnet.excuteCommand("rd /s /q " + FileUtil.getBaseName(source_file));

telnet.excuteCommand("cd \\was\\" + system_name + "\\bin");
telnet.excuteCommand("startup");

telnet.excuteCommand("exit");
telnet.disconnect();