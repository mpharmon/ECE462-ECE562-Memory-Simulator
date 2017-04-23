package edu.arizona.ece.memsim.model;

import java.util.ArrayList;

public class CacheStatisticsAnalysis extends CacheStatistics {
	public Integer ACCESS_HIGH;
	public Integer ACCESS_LOW;
	public Integer ACCESS_AVERAGE;
	public Integer READ_HIT_HIGH;
	public Integer READ_HIT_LOW;
	public Integer READ_HIT_AVERAGE;
	public Integer READ_MISS_HIGH;
	public Integer READ_MISS_LOW;
	public Integer READ_MISS_AVERAGE;
	public Integer BLOCKREAD_HIT_HIGH;
	public Integer BLOCKREAD_HIT_LOW;
	public Integer BLOCKREAD_HIT_AVERAGE;
	public Integer BLOCKREAD_MISS_HIGH;
	public Integer BLOCKREAD_MISS_LOW;
	public Integer BLOCKREAD_MISS_AVERAGE;
	public Integer BLOCKWRITE_HIT_HIGH;
	public Integer BLOCKWRITE_HIT_LOW;
	public Integer BLOCKWRITE_HIT_AVERAGE;
	public Integer BLOCKWRITE_MISS_HIGH;
	public Integer BLOCKWRITE_MISS_LOW;
	public Integer BLOCKWRITE_MISS_AVERAGE;
	public Integer WRITE_MISS_HIGH;
	public Integer WRITE_MISS_LOW;
	public Integer WRITE_MISS_AVERAGE;
	public Integer WRITE_HIT_HIGH;
	public Integer WRITE_HIT_LOW;
	public Integer WRITE_HIT_AVERAGE;
	public Integer REPLACEMENT_HIGH;
	public Integer REPLACEMENT_LOW;
	public Integer REPLACEMENT_AVERAGE;
	public Integer WRITEBACK_HIGH;
	public Integer WRITEBACK_LOW;
	public Integer WRITEBACK_AVERAGE;
	public Integer INVALIDATE_HIGH;
	public Integer INVALIDATE_LOW;
	public Integer INVALIDATE_AVERAGE;
	
	public CacheStatisticsAnalysis(){
		super();
		ACCESS_HIGH = new Integer(0);
		ACCESS_LOW = new Integer(999999999);
		ACCESS_AVERAGE = new Integer(0);
		READ_HIT_HIGH = new Integer(0);
		READ_HIT_LOW = new Integer(999999999);
		READ_HIT_AVERAGE = new Integer(0);
		READ_MISS_HIGH = new Integer(0);
		READ_MISS_LOW = new Integer(999999999);
		READ_MISS_AVERAGE = new Integer(0);
		BLOCKREAD_HIT_HIGH = new Integer(0);
		BLOCKREAD_HIT_LOW = new Integer(999999999);
		BLOCKREAD_HIT_AVERAGE = new Integer(0);
		BLOCKREAD_MISS_HIGH = new Integer(0);
		BLOCKREAD_MISS_LOW = new Integer(999999999);
		BLOCKREAD_MISS_AVERAGE = new Integer(0);
		BLOCKWRITE_HIT_HIGH = new Integer(0);
		BLOCKWRITE_HIT_LOW = new Integer(999999999);
		BLOCKWRITE_HIT_AVERAGE = new Integer(0);
		BLOCKWRITE_MISS_HIGH = new Integer(0);
		BLOCKWRITE_MISS_LOW = new Integer(999999999);
		BLOCKWRITE_MISS_AVERAGE = new Integer(0);
		WRITE_MISS_HIGH = new Integer(0);
		WRITE_MISS_LOW = new Integer(999999999);
		WRITE_MISS_AVERAGE = new Integer(0);
		WRITE_HIT_HIGH = new Integer(0);
		WRITE_HIT_LOW = new Integer(999999999);
		WRITE_HIT_AVERAGE = new Integer(0);
		REPLACEMENT_HIGH = new Integer(0);
		REPLACEMENT_LOW = new Integer(999999999);
		REPLACEMENT_AVERAGE = new Integer(0);
		WRITEBACK_HIGH = new Integer(0);
		WRITEBACK_LOW = new Integer(999999999);
		WRITEBACK_AVERAGE = new Integer(0);
		INVALIDATE_HIGH = new Integer(0);
		INVALIDATE_LOW = new Integer(999999999);
		INVALIDATE_AVERAGE = new Integer(0);
	}
	
	@Override
	public void print(String prefix){
		System.out.println(prefix + ".ACCESS_HIGH\t\t\t" + ACCESS_HIGH);
		System.out.println(prefix + ".ACCESS_LOW\t\t\t" + ACCESS_LOW);
		System.out.println(prefix + ".ACCESS_AVERAGE\t\t" + ACCESS_AVERAGE);
		
		System.out.println(prefix + ".READ_HIT_HIGH\t\t" + READ_HIT_HIGH);
		System.out.println(prefix + ".READ_HIT_LOW\t\t\t" + READ_HIT_LOW);
		System.out.println(prefix + ".READ_HIT_AVERAGE\t\t" + READ_HIT_AVERAGE);
		
		System.out.println(prefix + ".READ_MISS_LOW\t\t" + READ_MISS_LOW);
		System.out.println(prefix + ".READ_MISS_HIGH\t\t" + READ_MISS_HIGH);
		System.out.println(prefix + ".READ_MISS_AVERAGE\t\t" + READ_MISS_AVERAGE);
		
		System.out.println(prefix + ".BLOCKREAD_HIT_LOW\t\t" + BLOCKREAD_HIT_LOW);
		System.out.println(prefix + ".BLOCKREAD_HIT_HIGH\t\t" + BLOCKREAD_HIT_HIGH);
		System.out.println(prefix + ".BLOCKREAD_HIT_AVERAGE\t" + BLOCKREAD_HIT_AVERAGE);
		
		System.out.println(prefix + ".BLOCKREAD_MISS_HIGH\t\t" + BLOCKREAD_MISS_HIGH);
		System.out.println(prefix + ".BLOCKREAD_MISS_LOW\t\t" + BLOCKREAD_MISS_LOW);
		System.out.println(prefix + ".BLOCKREAD_MISS_AVERAGE\t" + BLOCKREAD_MISS_AVERAGE);
		
		System.out.println(prefix + ".WRITE_HIT_HIGH\t\t" + WRITE_HIT_HIGH);
		System.out.println(prefix + ".WRITE_HIT_LOW\t\t" + WRITE_HIT_LOW);
		System.out.println(prefix + ".WRITE_HIT_AVERAGE\t\t" + WRITE_HIT_AVERAGE);
		
		System.out.println(prefix + ".WRITE_MISS_HIGH\t\t" + WRITE_MISS_HIGH);
		System.out.println(prefix + ".WRITE_MISS_LOW\t\t" + WRITE_MISS_LOW);
		System.out.println(prefix + ".WRITE_MISS_AVERAGE\t\t" + WRITE_MISS_AVERAGE);
		
		System.out.println(prefix + ".BLOCKWRITE_HIT_HIGH\t\t" + BLOCKWRITE_HIT_HIGH);
		System.out.println(prefix + ".BLOCKWRITE_HIT_LOW\t\t" + BLOCKWRITE_HIT_LOW);
		System.out.println(prefix + ".BLOCKWRITE_HIT_AVERAGE\t" + BLOCKWRITE_HIT_AVERAGE);
		
		System.out.println(prefix + ".BLOCKWRITE_MISS_HIGH\t\t" + BLOCKWRITE_MISS_HIGH);
		System.out.println(prefix + ".BLOCKWRITE_MISS_LOW\t\t" + BLOCKWRITE_MISS_LOW);
		System.out.println(prefix + ".BLOCKWRITE_MISS_AVERAGE\t" + BLOCKWRITE_MISS_AVERAGE);
		
		// TODO: Uncomment When Cache Coherancy Is Implemented
		//System.out.println(prefix + ".REPLACEMENT\t\t" + REPLACEMENT);
		//System.out.println(prefix + ".WRITEBACK\t\t\t" + WRITEBACK);
		//System.out.println(prefix + ".INVALIDATE\t\t\t" + INVALIDATE);
	}
	
	public static CacheStatisticsAnalysis AnalyzeArrayList(ArrayList<CacheStatistics> arrayList){
		CacheStatisticsAnalysis cacheStatsAnalysis = new CacheStatisticsAnalysis();
		
		for(CacheStatistics stats : arrayList){
			cacheStatsAnalysis.ACCESS += stats.ACCESS;
			if(stats.ACCESS.intValue() > cacheStatsAnalysis.ACCESS_HIGH.intValue())cacheStatsAnalysis.ACCESS_HIGH = stats.ACCESS;
			if(stats.ACCESS.intValue() < cacheStatsAnalysis.ACCESS_LOW.intValue())cacheStatsAnalysis.ACCESS_LOW = stats.ACCESS;
			
			cacheStatsAnalysis.READ_HIT += stats.READ_HIT;
			if(stats.READ_HIT.intValue() > cacheStatsAnalysis.READ_HIT_HIGH.intValue())cacheStatsAnalysis.READ_HIT_HIGH = stats.READ_HIT;
			if(stats.READ_HIT.intValue() < cacheStatsAnalysis.READ_HIT_LOW.intValue())cacheStatsAnalysis.READ_HIT_LOW = stats.READ_HIT;
			
			cacheStatsAnalysis.READ_MISS += stats.READ_MISS;
			if(stats.READ_MISS.intValue() > cacheStatsAnalysis.READ_MISS_HIGH.intValue())cacheStatsAnalysis.READ_MISS_HIGH = stats.READ_MISS;
			if(stats.READ_MISS.intValue() < cacheStatsAnalysis.READ_MISS_LOW.intValue())cacheStatsAnalysis.READ_MISS_LOW = stats.READ_MISS;
			
			cacheStatsAnalysis.WRITE_HIT += stats.WRITE_HIT;
			if(stats.WRITE_HIT.intValue() > cacheStatsAnalysis.WRITE_HIT_HIGH.intValue())cacheStatsAnalysis.WRITE_HIT_HIGH = stats.WRITE_HIT;
			if(stats.WRITE_HIT.intValue() < cacheStatsAnalysis.WRITE_HIT_LOW.intValue())cacheStatsAnalysis.WRITE_HIT_LOW = stats.WRITE_HIT;
			
			cacheStatsAnalysis.WRITE_MISS += stats.WRITE_MISS;
			if(stats.WRITE_MISS.intValue() > cacheStatsAnalysis.WRITE_MISS_HIGH.intValue())cacheStatsAnalysis.WRITE_MISS_HIGH = stats.WRITE_MISS;
			if(stats.WRITE_MISS.intValue() < cacheStatsAnalysis.WRITE_MISS_LOW.intValue())cacheStatsAnalysis.WRITE_MISS_LOW = stats.WRITE_MISS;
			
			cacheStatsAnalysis.BLOCKREAD_HIT += stats.BLOCKREAD_HIT;
			if(stats.BLOCKREAD_HIT.intValue() > cacheStatsAnalysis.BLOCKREAD_HIT_HIGH.intValue())cacheStatsAnalysis.BLOCKREAD_HIT_HIGH = stats.BLOCKREAD_HIT;
			if(stats.BLOCKREAD_HIT.intValue() < cacheStatsAnalysis.BLOCKREAD_HIT_LOW.intValue())cacheStatsAnalysis.BLOCKREAD_HIT_LOW = stats.BLOCKREAD_HIT;
			
			cacheStatsAnalysis.BLOCKREAD_MISS += stats.BLOCKREAD_MISS;
			if(stats.BLOCKREAD_MISS.intValue() > cacheStatsAnalysis.BLOCKREAD_MISS_HIGH.intValue())cacheStatsAnalysis.BLOCKREAD_MISS_HIGH = stats.BLOCKREAD_MISS;
			if(stats.BLOCKREAD_MISS.intValue() < cacheStatsAnalysis.BLOCKREAD_MISS_LOW.intValue())cacheStatsAnalysis.BLOCKREAD_MISS_LOW = stats.BLOCKREAD_MISS;
			
			cacheStatsAnalysis.BLOCKWRITE_HIT += stats.BLOCKWRITE_HIT;
			if(stats.BLOCKWRITE_HIT.intValue() > cacheStatsAnalysis.BLOCKWRITE_HIT_HIGH.intValue())cacheStatsAnalysis.BLOCKWRITE_HIT_HIGH = stats.BLOCKWRITE_HIT;
			if(stats.BLOCKWRITE_HIT.intValue() < cacheStatsAnalysis.BLOCKWRITE_HIT_LOW.intValue())cacheStatsAnalysis.BLOCKWRITE_HIT_LOW = stats.BLOCKWRITE_HIT;
			
			cacheStatsAnalysis.BLOCKWRITE_MISS += stats.BLOCKWRITE_MISS;
			if(stats.BLOCKWRITE_MISS.intValue() > cacheStatsAnalysis.BLOCKWRITE_MISS_HIGH.intValue())cacheStatsAnalysis.BLOCKWRITE_MISS_HIGH = stats.BLOCKWRITE_MISS;
			if(stats.BLOCKWRITE_MISS.intValue() < cacheStatsAnalysis.BLOCKWRITE_MISS_LOW.intValue())cacheStatsAnalysis.BLOCKWRITE_MISS_LOW = stats.BLOCKWRITE_MISS;
			
			cacheStatsAnalysis.REPLACEMENT += stats.REPLACEMENT;
			if(stats.REPLACEMENT.intValue() > cacheStatsAnalysis.REPLACEMENT_HIGH.intValue())cacheStatsAnalysis.REPLACEMENT_HIGH = stats.REPLACEMENT;
			if(stats.REPLACEMENT.intValue() < cacheStatsAnalysis.REPLACEMENT_LOW.intValue())cacheStatsAnalysis.REPLACEMENT_LOW = stats.REPLACEMENT;
			
			cacheStatsAnalysis.WRITEBACK += stats.WRITEBACK;
			if(stats.WRITEBACK.intValue() > cacheStatsAnalysis.WRITEBACK_HIGH.intValue())cacheStatsAnalysis.WRITEBACK_HIGH = stats.WRITEBACK;
			if(stats.WRITEBACK.intValue() < cacheStatsAnalysis.WRITEBACK_LOW.intValue())cacheStatsAnalysis.WRITEBACK_LOW = stats.WRITEBACK;
			
			cacheStatsAnalysis.INVALIDATE += stats.INVALIDATE;
			if(stats.INVALIDATE.intValue() > cacheStatsAnalysis.INVALIDATE_HIGH.intValue())cacheStatsAnalysis.INVALIDATE_HIGH = stats.INVALIDATE;
			if(stats.INVALIDATE.intValue() < cacheStatsAnalysis.INVALIDATE_LOW.intValue())cacheStatsAnalysis.INVALIDATE_LOW = stats.INVALIDATE;
		}
		cacheStatsAnalysis.ACCESS_AVERAGE = cacheStatsAnalysis.ACCESS / arrayList.size();
		cacheStatsAnalysis.READ_HIT_AVERAGE = cacheStatsAnalysis.READ_HIT / arrayList.size();
		cacheStatsAnalysis.READ_MISS_AVERAGE = cacheStatsAnalysis.READ_MISS / arrayList.size();
		cacheStatsAnalysis.WRITE_HIT_AVERAGE = cacheStatsAnalysis.WRITE_HIT / arrayList.size();
		cacheStatsAnalysis.WRITE_MISS_AVERAGE = cacheStatsAnalysis.WRITE_MISS / arrayList.size();
		cacheStatsAnalysis.BLOCKREAD_HIT_AVERAGE = cacheStatsAnalysis.BLOCKREAD_HIT / arrayList.size();
		cacheStatsAnalysis.BLOCKREAD_MISS_AVERAGE = cacheStatsAnalysis.BLOCKREAD_MISS / arrayList.size();
		cacheStatsAnalysis.BLOCKWRITE_HIT_AVERAGE = cacheStatsAnalysis.BLOCKWRITE_HIT / arrayList.size();
		cacheStatsAnalysis.BLOCKWRITE_MISS_AVERAGE = cacheStatsAnalysis.BLOCKWRITE_MISS / arrayList.size();
		cacheStatsAnalysis.REPLACEMENT_AVERAGE = cacheStatsAnalysis.REPLACEMENT / arrayList.size();
		cacheStatsAnalysis.WRITEBACK_AVERAGE = cacheStatsAnalysis.WRITEBACK / arrayList.size();
		cacheStatsAnalysis.INVALIDATE_AVERAGE = cacheStatsAnalysis.INVALIDATE / arrayList.size();
		
		return cacheStatsAnalysis;
	}
}
