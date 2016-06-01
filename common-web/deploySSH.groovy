import org.oh.common.util.FileUtil;
import org.oh.common.util.SFTPUtil;
import org.oh.common.util.SSHUtil;

String os_name = "Linux";
String system_name = "cbms";
String source_dir = "target";
String target_dir = "/was/" + system_name + "/webapps";
String source_file = "v1#" + system_name + ".war";

String server_ip = "112.217.207.164";
int server_port = 20022;
String user_id = "oracle";
String user_pw = "nemustech";

String title = "Deploy \"" + server_ip + "\" server \"" + system_name + "\" system";

// 파일 복사
File destFile = new File(source_dir + "/" + source_file);
FileUtil.copyFile(new File(source_dir + "/" + project.properties["name"] + "-" + project.properties["version"] + ".war"), destFile);

// 배포 및 WAS 재가동
System.out.println("---------- " + title + " ----------");
System.out.println("--- Sending the war file to \"" + server_ip + "\"");
SFTPUtil ftp = new SFTPUtil(server_ip, server_port, user_id, user_pw);
ftp.backup(target_dir, source_file);
ftp.upload(source_dir, source_file, target_dir);
ftp.disconnect();

System.out.println("--- Restarting the WAS container \"" + system_name + "\"");
SSHUtil ssh = new SSHUtil(server_ip, server_port, user_id, user_pw, os_name);
ssh.excuteCommand("cd /was/" + system_name + "/bin");
ssh.excuteCommand("./shutdown.sh");

ssh.excuteCommand("cd " + target_dir);
ssh.excuteCommand("rm -r v1#" + system_name);

ssh.excuteCommand("cd /was/" + system_name + "/bin");
ssh.excuteCommand("./startup.sh");

ssh.excuteCommand("exit");
ssh.disconnect();

// 파일 삭제
FileUtil.deleteQuietly(destFile);