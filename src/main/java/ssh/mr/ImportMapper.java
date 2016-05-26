package ssh.mr;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Mapper类，接收HDFS数据，写入到HBase表中
 * @author fansy
 *
 */
public class ImportMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put>{
	private static final String COMMA = ",";
	private static final String COLON=":";
	private String splitter = null;
//	private String colsStr = null;
	
	private int rkIndex =0; // rowkey 下标
	private int tsIndex =1; // timestamp下标
	private boolean hasTs = false; // 原始数据是否有timestamp
	
	private SimpleDateFormat sf = null;
	
	private ArrayList<byte[][]> colsFamily= null;
	
	private Put put =null;

	ImmutableBytesWritable rowkey = new ImmutableBytesWritable();
	@Override
	protected void setup(Mapper<LongWritable, Text, ImmutableBytesWritable, Put>.Context context)
			throws IOException, InterruptedException {
		splitter = context.getConfiguration().get(ImportToHBase.SPLITTER,",");
		String colsStr = context.getConfiguration().get(ImportToHBase.COLSFAMILY,null);
		
		sf = context.getConfiguration().get(ImportToHBase.DATEFORMAT,null)==null
				? new SimpleDateFormat("yyyy-MM-dd HH:mm")
						:new SimpleDateFormat(context.getConfiguration().get(ImportToHBase.DATEFORMAT));
		
		String[] cols = colsStr.split(COMMA, -1);
		
		colsFamily =new ArrayList<>();
		for(int i=0;i< cols.length;i++){
			if("rk".equals(cols[i])){
				rkIndex= i;
				colsFamily.add(null);
				continue;
			}
			if("ts".equals(cols[i])){
				tsIndex = i;
				colsFamily.add(null);
				hasTs = true; // 原始数据包括ts
				continue;
			}
			colsFamily.add(getCol(cols[i]));
		}
		
	}
	/**
	 * 获取 family：qualifier byte数组
	 * @param col
	 * @return
	 */
	private byte[][] getCol(String col) {
		byte[][] fam_qua = new byte[2][];
		String[] fam_quaStr = col.split(COLON, -1);
		fam_qua[0]=  Bytes.toBytes(fam_quaStr[0]);
		fam_qua[1]=  Bytes.toBytes(fam_quaStr[1]);
	
		return fam_qua;
	}

	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, ImmutableBytesWritable, Put>.Context context)
					throws IOException, InterruptedException {
		String[] words = value.toString().split(splitter, -1);
		
		if(words.length!=colsFamily.size()){
			System.out.println("line:"+value.toString()+" does not compatible!");
			return ;
		}
		
		rowkey.set(getRowKey(words[rkIndex]));
		
		put = getValue(words,colsFamily,rowkey.copyBytes());
		
		context.write(rowkey, put);
		
	}
	/**
	 * 获取Put值
	 * @param words
	 * @param colsFamily
	 * @param bs
	 * @return
	 */
	private Put getValue(String[] words, ArrayList<byte[][]> colsFamily, byte[] bs) {
		Put put = new Put(bs);
		
		for(int i=0;i<colsFamily.size();i++){
			if(colsFamily.get(i)==null){// rk 或ts
				continue;// 下一个 列
			}
			if(words[i]==null || words[i].length()==0) {
				// 不添加，直接往下一个value
				continue;
			}
			// 日期异常的记录同样添加
			if(hasTs){// 插入包含时间的数据
				put.addColumn(colsFamily.get(i)[0], colsFamily.get(i)[1],
					getLongFromDate(words[tsIndex]), Bytes.toBytes(words[i]));
			}else{// 不包含时间的数据
				put.addColumn(colsFamily.get(i)[0], colsFamily.get(i)[1],
						 Bytes.toBytes(words[i]));
			}
			
		}
		
		return put;
	}
	private long getLongFromDate(String dateStr)  {
		try{
			return sf.parse(dateStr).getTime();
		}catch(ParseException e){
			System.out.println(dateStr+" 转换失败!");
			return 0;
		}
	}
	/**
	 * 获取rowkey byte数组
	 * @param rowKey
	 * @return
	 */
	private byte[] getRowKey(String rowKey) {
		
		return Bytes.toBytes(rowKey);
	}

}
