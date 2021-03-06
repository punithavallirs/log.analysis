package etl.engine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import etl.util.Util;

public abstract class ETLCmd {
	public static final Logger logger = Logger.getLogger(ETLCmd.class);
	
	public static final String RESULT_KEY_LOG="log";
	public static final String RESULT_KEY_OUTPUT="lineoutput";
	public static final String RESULT_KEY_OUTPUT_MAP="mapoutput";

	public static final String SINGLE_TABLE="single.table";
	
	protected String wfid;
	protected FileSystem fs;
	protected PropertiesConfiguration pc;
	protected String[] otherArgs;

	private Configuration conf;
	
	private ProcessMode pm = ProcessMode.SingleProcess;
	private MRMode mrMode = MRMode.file;
	private boolean sendLog = true;//command level send log flag
	
	//system variable map
	public static final String VAR_WFID="WFID"; //string type 
	public static final String VAR_FIELDS="fields"; //string[] type
	private Map<String, Object> systemVariables = new HashMap<String, Object>();
	
	
	public ETLCmd(String wfid, String staticCfg, String defaultFs, String[] otherArgs){
		this.wfid = wfid;
		try {
			String fs_key = "fs.defaultFS";
			conf = new Configuration();
			if (defaultFs!=null){
				conf.set(fs_key, defaultFs);
			}
			logger.info(String.format("%s is %s", fs_key, conf.get(fs_key)));
			this.fs = FileSystem.get(conf);
		}catch(Exception e){
			logger.error("", e);
		}
		pc = Util.getMergedPCFromDfs(fs, staticCfg);
		this.otherArgs = otherArgs;
		systemVariables.put(VAR_WFID, wfid);
	}
	
	public Map<String, Object> getSystemVariables(){
		return systemVariables;
	}
	
	/**
	 * @return map may contains following key:
	 * RESULT_KEY_LOG: list of String user defined log info
	 * RESULT_KEY_LIST_OUTPUT: list of String output
	 * in the value map, if it contains only 1 value, the key is RESULT_VALUE_KEY_SINGLE_VALUE
	 */
	public Map<String, Object> mapProcess(long offset, String row, Mapper<LongWritable, Text, Text, NullWritable>.Context context){
		logger.error("empty map impl, should not be invoked.");
		return null;
	}
	
	/**
	 * @return map
	 */
	public Map<String, String> reduceMapProcess(long offset, String row, Mapper<LongWritable, Text, Text, Text>.Context context){
		logger.error("empty reduce-map impl, should not be invoked.");
		return null;
	}
	
	/**
	 * @return list of newKey, newValue, baseOutputPath
	 */
	public List<String[]> reduceProcess(Text key, Iterable<Text> values){
		logger.error("empty reduce impl, should not be invoked.");
		return null;
	}
	
	/**
	 * @return list of String user defined log info
	 */
	public List<String> sgProcess(){
		logger.error("empty single impl, should not be invoked.");
		return null;
	}
	
	///

	public Configuration getHadoopConf(){
		return conf;
	}
	
	public ProcessMode getPm() {
		return pm;
	}

	public void setPm(ProcessMode pm) {
		this.pm = pm;
	}
	
	public MRMode getMrMode() {
		return mrMode;
	}

	public void setMrMode(MRMode mrMode) {
		this.mrMode = mrMode;
	}
	
	public String getWfid(){
		return wfid;
	}
	
	public PropertiesConfiguration getPc() {
		return pc;
	}

	public void setPc(PropertiesConfiguration pc) {
		this.pc = pc;
	}

	public boolean isSendLog() {
		return sendLog;
	}

	public void setSendLog(boolean sendLog) {
		this.sendLog = sendLog;
	}
}
