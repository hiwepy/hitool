/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 *  线程Executor工具类
 */
public abstract class ExecutorUtils {

	protected static Logger LOG = LoggerFactory.getLogger(ExecutorUtils.class);
	
	public static int getBatchSize(int count, int thread_num, int betch_index) {
		
		int betch_size = (count - count % thread_num) / thread_num;
		int betch_count = (count - count % thread_num) / (betch_size == 0 ? 1 : betch_size);
		if (count % thread_num > 0) {
			if (betch_index > betch_count + 1) {
				throw new IndexOutOfBoundsException("betch_index out of index !");
			} else {
				if (betch_index == betch_count + 1) {
					return count % thread_num;
				} else {
					return betch_size;
				}
			}
		} else {
			if (betch_index > betch_count) {
				throw new IndexOutOfBoundsException("betch_index out of index !");
			} else {
				return betch_size;
			}
		}
	}
	
	public static int getThreadCount(int count,int batch_size){
		//得到当前sheet 的总行数，并计算当前sheet需要多少个线程进行导入操作
		int thread_num = 1;
		if(count > batch_size){
			thread_num = (count - count%batch_size)/batch_size;
			if(count%batch_size>0){
				thread_num = thread_num+1;
			}
		}
		return thread_num;
	}
	
	/*
	 * <p>1、调用shutdown()停止接受任何新的任务且等待已经提交的任务执行完成</p>
	 * <p>2、调用awaitTermination(awaitTime, TimeUnit) 每隔10秒钟检测ExecutorService的关闭情况</p>
	 */
	public static int awaitShutdown(ExecutorService cachedThreadPool ){
		return awaitShutdown(cachedThreadPool, 10, TimeUnit.SECONDS);
	}
	
	/*
	 * <p>1、调用shutdown()停止接受任何新的任务且等待已经提交的任务执行完成</p>
	 * <p>2、调用awaitTermination(awaitTime, TimeUnit) 每隔指定时间监测一次ExecutorService的关闭情况</p>
	 */
	public static int awaitShutdown(ExecutorService cachedThreadPool, int awaitTime, TimeUnit unit ){
		long time =	System.currentTimeMillis();
		try {
			/*
			 * 这个方法会平滑地关闭ExecutorService，当我们调用这个方法时，
			 * ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成
			 * (已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，
			 * 当所有已经提交的任务执行完毕后将会关闭ExecutorService。
			 */
			cachedThreadPool.shutdown(); 
			//=====================等待所有任务完成 =========================
			/*
			 * 这个方法有两个参数，一个是timeout即超时时间，另一个是unit即时间单位。
			 * 这个方法会使线程等待timeout时长，当超过timeout时间后，会监测ExecutorService是否已经关闭，
			 * 若关闭则返回true，否则返回false。
			 * 一般情况下会和shutdown方法组合使用
			 * 
			 * 注意：这里每隔指定时间监测一次ExecutorService的关闭情况(所有的任务都结束的时候，返回TRUE)
			 */
			while (!cachedThreadPool.awaitTermination(awaitTime, unit)) {  
				// 记录运行状态信息
				LOG.info("Thread [" + Thread.currentThread().getName() + "] Runing...");
			}
			// 记录完成信息
			LOG.info("Thread [" + Thread.currentThread().getName() + "] shutdown.");
	    } catch (InterruptedException ie) {
	    	// awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
	        System.out.println("awaitTermination interrupted: " + ie);
	        cachedThreadPool.shutdownNow();
	    }
	    //返回任务执行时间
		return Double.valueOf(Math.ceil(System.currentTimeMillis() - time / 1000)).intValue();
	}
	
	public static int shutdown(ExecutorService cachedThreadPool){
		/*
		 * 这个方法会平滑地关闭ExecutorService，当我们调用这个方法时，
		 * ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成
		 * (已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，
		 * 当所有已经提交的任务执行完毕后将会关闭ExecutorService。
		 */
		cachedThreadPool.shutdown(); 
		//=====================等待所有任务完成 =========================
		/*
		 * 这个方法会校验ExecutorService当前的状态是否为“TERMINATED”即关闭状态，当为“TERMINATED”时返回true否则返回false
		 * 注意：该方法执行循环较快，可做完成时间统计
		 */
		long time =	System.currentTimeMillis();
		while (!cachedThreadPool.isTerminated()) { 
			// 记录运行状态信息
			LOG.info("Thread [" + Thread.currentThread().getName() + "] Runing...");
		}
		//返回任务执行时间
		return Double.valueOf(Math.ceil(System.currentTimeMillis() - time / 1000)).intValue();
	}
	
}
