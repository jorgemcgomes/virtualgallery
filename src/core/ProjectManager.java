package core;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import core.module.OperationModule;


class ProjectManager {

	private OperationModule[] modes;
	private Map<String, Serializable> lastStatus;
	private File saveDest;

	ProjectManager(OperationModule[] modes) {
		this.modes = modes;
		lastStatus = null;
	}
	
	File save(File destiny, ProjectConfiguration conf) throws IOException {
		if(destiny == null)
			destiny = saveDest;
		
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("configuration", conf);
		for(OperationModule m : modes) {
			map.put(m.getClass().getSimpleName(), m.getStatus());
		}

		BufferedOutputStream buffOut = new BufferedOutputStream(new FileOutputStream(destiny));
		ObjectOutputStream objOut = new ObjectOutputStream(buffOut);
		objOut.writeObject(map);
		objOut.close();
		buffOut.close();
		
		saveDest = destiny;
		return saveDest;
	}
	
	boolean hasSaveLocation() {
		return saveDest != null && saveDest.exists();
	}

	@SuppressWarnings("unchecked")
	ProjectConfiguration load(File projectFile) {
		ProjectConfiguration conf = null;
		try {	
			BufferedInputStream buffIn = new BufferedInputStream(new FileInputStream(projectFile));
			ObjectInputStream objIn = new ObjectInputStream(buffIn);
			lastStatus = (Map<String, Serializable>) objIn.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if(lastStatus == null)
			return null;

		conf = (ProjectConfiguration) lastStatus.get("configuration");
		saveDest = projectFile;
		return conf;
	}

	void restoreLastStatus() {
		if(lastStatus != null)
			for(OperationModule m : modes) {
				m.reset();
				Serializable status = lastStatus.get(m.getClass().getSimpleName());
				if(status != null)
					m.restoreStatus(status);
			}
	}
}
