import java.text.MessageFormat

import org.apache.commons.io.FileUtils;
import com.nemustech.common.util.SSHUtil

String os_name = "Linux";
String[] system_names = [
	//	"aams",
	//	"bms",
	//	"cbms",
	//	"devms",
	//	"lms",
	//	"mms",
	//	"zcms",
	"zms",
	//	"cpgn",
	//	"admin"
];
//String source_dir = "target";


String[] server_ips = [
	"112.217.207.164"
];
int server_port = 20022;
String user_id = "oracle";
String user_pw = "nemustech";

for (String server_ip : server_ips) {
	SSHUtil ssh = new SSHUtil(server_ip, server_port, user_id, user_pw, os_name);

	for (String system_name : system_names) {
		String title = "Target \"" + server_ip + "\" server \"" + system_name + "\" system";
		System.out.println("---------- " + title + " ----------");

		File file = new File("groovy/work.sh");
		System.out.println(file.getAbsolutePath());
		List<String> list = FileUtils.readLines(file, "UTF-8");
		for (int i = 0; i < list.size(); i++) {
			String cmd = list.get(i);
			if (i == 0) {
				cmd = MessageFormat.format(cmd, (Object[]) [system_name]);
			}

			ssh.excuteCommand(cmd);
		}
	}

	ssh.excuteCommand("exit");
	ssh.disconnect();
}