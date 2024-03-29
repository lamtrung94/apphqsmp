package lx.tung.knockcode;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;


public class SysUtils {
	private final static String cpufreq_sys_dir = "/sys/devices/system/cpu/cpu0/cpufreq/";
	private final static String scaling_min_freq = cpufreq_sys_dir + "scaling_min_freq";
	private final static String cpuinfo_min_freq = cpufreq_sys_dir + "cpuinfo_min_freq";
	private final static String scaling_max_freq = cpufreq_sys_dir + "scaling_max_freq";
	private final static String cpuinfo_max_freq = cpufreq_sys_dir + "cpuinfo_max_freq";
	private final static String scaling_cur_freq = cpufreq_sys_dir + "scaling_cur_freq";
	private final static String cpuinfo_cur_freq = cpufreq_sys_dir + "cpuinfo_cur_freq";
	private final static String scaling_governor = cpufreq_sys_dir + "scaling_governor";
	private final static String cpu_enable_dir = "/sys/devices/system/cpu/cpu0/online";
	
	
	private final static String scaling_available_freq = cpufreq_sys_dir + "scaling_available_frequencies";
	private final static String scaling_available_governors = cpufreq_sys_dir + "scaling_available_governors";
	private final static String scaling_stats_time_in_state = cpufreq_sys_dir + "stats/time_in_state";

	private final static String ioscheduler = "/sys/block/mmcblk0/queue/scheduler";
	/**
	 * Certain devices (i.e.: Minix X5) don't have the mmcblk0 device, and use a
	 * different name for MTD devices. Note that we only use this file to read the
	 * available schedulers (and the currently set one), since while writing we
	 * iterate on every avalaible file matching /sys/block/xxx/queue/scheduler
	 * which isn't a fake device.
	 */
	private final static String ioscheduler_mtd = "/sys/block/mtdblock0/queue/scheduler";
	private final static String RE_FAKE_BLKDEV = "(loop|zram|dm-)[0-9]+";

	private final static int BUFFER_SIZE = 2048;
	
	public static String getSUbinaryPath() {
		String s = "/system/bin/su";
		File f = new File(s);
		if (f.exists()) {
			return s;
		}
		s = "/system/xbin/su";
		f = new File(s);
		if (f.exists()) {
			return s;
		}
		return null;
	}
	
	private static String readString(String filename) {
		try {
			File f = new File(filename);
			if (f.exists()) {
				InputStream is = null;
				if (f.canRead()) {
					is = new FileInputStream(f);
				} else {
					//Log.w(Constants.APP_TAG, "read-only file, trying w/ root: " + filename);
					/*
					 * Try reading as root.
					 */
					String[] commands = {
							"cat " + filename + "\n", "exit\n"
					};
					Process p = Runtime.getRuntime().exec(getSUbinaryPath());
					DataOutputStream dos = new DataOutputStream(p.getOutputStream());
					for (String command : commands) {
						dos.writeBytes(command);
						dos.flush();
					}
					if (p.waitFor() == 0) {
						is = p.getInputStream();
					} else {
						// is = p.getErrorStream();
						return null;
					}
				} // end-if: f.canRead()
				BufferedReader br = new BufferedReader(new InputStreamReader(is), BUFFER_SIZE);
				String line = br.readLine();
				br.close();
				return line;
			} else {
				/*
				 * File does not exist.
				 */
				Log.e("KNOCK ON", "file does not exist: " + filename);
				return null;
			}
		} catch (InterruptedException iex) {
			Log.e("KNOCK ON", iex.getMessage(), iex);
			return null;
		} catch (IOException ioex) {
			Log.e("KNOCK ON", ioex.getMessage(), ioex);
			return null;
		}
	}
	
	public static Frequency getCurFreq() {
		String[] files1 = {
				scaling_cur_freq, cpuinfo_cur_freq
		};
		int cpus = 0;
		while (true) {
			File f = new File(SysUtils.scaling_min_freq.replace("cpu0", "cpu" + cpus));
			if (!f.exists()) {
				break;
			}
			cpus++;
		}
		for (int i = 0; i < cpus; i++){
			String[] files = {
					scaling_cur_freq.replace("cpu0", "cpu" + i), cpuinfo_cur_freq.replace("cpu0", "cpu" + i)
			};
			Frequency freq = getXXXFreq(files);
			System.out.println();
		}
		return getXXXFreq(files1);
	}
	
	
	private static Frequency getXXXFreq(String[] possibleFiles) {
		for (String file : possibleFiles) {
			String s = readString(file);
			if (s != null) {
				return new Frequency(s);
			}
		}
		return null;
	}
	
	public static Frequency getMinFreq() {
		String[] files = {
				scaling_min_freq, cpuinfo_min_freq
		};
		return getXXXFreq(files);
	}
	
	public static Frequency getMaxFreq() {
		String[] files = {
				scaling_max_freq, cpuinfo_max_freq
		};
		return getXXXFreq(files);
	}
	
	public static String getGovernor() {
		return readString(scaling_governor);
	}
	
	public static String[] getAvailableGovernors() {
		return readStringArray(scaling_available_governors);
	}
	
	private static String[] readStringArray(String filename) {
		String line = readString(filename);
		if (line != null) {
			return line.split(" ");
		}
		return null;
	}
	
	public static String getIOScheduler() {
		String scheduler = null;
		String file = ioscheduler;
		if (!(new File(file)).exists()) {
			file = ioscheduler_mtd;
		}
		String[] schedulers = readStringArray(file);
		if (schedulers != null) {
			for (String s : schedulers) {
				if (s.charAt(0) == '[') {
					scheduler = s.substring(1, s.length() - 1);
					break;
				}
			}
		}
		return scheduler;
	}
	
	public static Stats getFrequencyStats(boolean withDeepSleep) {
		Map<String, Long> times = new HashMap<String, Long>();
		List<Frequency> frequencies = new ArrayList<Frequency>();
		Long totalTime = 0L;
		File f = new File(scaling_stats_time_in_state);
		InputStream is = null;
		if (f.exists()) {
			try {
				if (f.canRead()) {
					is = new FileInputStream(f);
				} else {
					Log.w("KNOCK ON", "read-only file, trying w/ root: " + f);
					/*
					 * Try reading as root
					 */
					String[] commands = {
							"cat " + f + "\n", "exit\n"
					};
					Process p = Runtime.getRuntime().exec(getSUbinaryPath());
					DataOutputStream dos = new DataOutputStream(p.getOutputStream());
					for (String command : commands) {
						dos.writeBytes(command);
						dos.flush();
					}
					if (p.waitFor() == 0) {
						is = p.getInputStream();
					} else {
						is = p.getErrorStream();
					}
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(is), BUFFER_SIZE);
				String line = null;
				while ((line = br.readLine()) != null) {
					String[] aux = line.split(" ");
					Frequency freq = new Frequency(aux[0]);
					Long time_in_state = Long.parseLong(aux[1]);
					totalTime += time_in_state;
					frequencies.add(freq);
					times.put(freq.getValue(), time_in_state);
				}
				br.close();
				if (withDeepSleep) {
					/* Add deep sleep to the values */
					Frequency deepSleep = new Frequency("-1");
					long uptimeInMillis = SystemClock.uptimeMillis();
					long elapsed = SystemClock.elapsedRealtime();
					long deepSleepTime = elapsed - uptimeInMillis;
					totalTime += deepSleepTime;
					frequencies.add(deepSleep);
					times.put(deepSleep.getValue(), deepSleepTime);
//					if (Constants.DEBUG) {
//						Log.d(Constants.APP_TAG, "deepSleep: " + deepSleepTime + ", elapsed: " + elapsed + ", uptime: "
//								+ uptimeInMillis);
//					}
				}
				return new Stats(frequencies, times, totalTime);
			} catch (IOException ioex) {
				Log.e("KNOCK ON", ioex.getMessage(), ioex);
				return null;
			} catch (InterruptedException iex) {
				Log.e("KNOCK ON", iex.getMessage(), iex);
				return null;
			}
		} else {
			Log.e("KNOCK ON", "file does not exist: " + f);
			return null;
		}

	}
	
	public static String[] getAvailableFrequencies() {
		String[] frequencies = readStringArray(scaling_available_freq);
		if (frequencies == null) {
			Log.e("KNOCK ON", "scaling_available_frequencies DOES NOT EXIST, trying different route: stats");
			Stats stats = getFrequencyStats(false);
			if (stats != null) {
				frequencies = new String[stats.getFrequencies().size()];
				for (int i = 0; i < stats.getFrequencies().size(); i++) {
					frequencies[i] = stats.getFrequencies().get(i).getValue();
				}
			} else {
				Log.e("KNOCK ON", "stats/time_in_state DOES NOT EXIST, trying different route: cpuinfo_*_freq");
				frequencies = new String[2];
				frequencies[0] = readString(cpuinfo_min_freq);
				frequencies[1] = readString(cpuinfo_max_freq);
				if (frequencies[0] == null || frequencies[1] == null) {
					Log.e("KNOCK ON", "cpuinfo_*_freq DO NOT EXIST! returning null");
					frequencies = null;
				}
			}
		}
		if (frequencies != null) {
			/*
			 * Sort them if they aren't already sorted. It happens on devices from
			 * Samsung. Thanks to JoPhj for the discovery.
			 */
			Comparator<String> frequencyComparator = new Comparator<String>() {
				@Override
				public int compare(String lhs, String rhs) {
					Integer lhi = Integer.parseInt(lhs);
					Integer rhi = Integer.parseInt(rhs);
					return lhi.compareTo(rhi);
				}
			};
			Arrays.sort(frequencies, frequencyComparator);
		}
		return frequencies;
	}
	
	public static boolean disableCores(Context ctx, int resOkMsg, int resFailedMsg){
		try {
			List<String> commands = new ArrayList<String>();

			int cpus = 0;
			while (true) {
				File f = new File(SysUtils.scaling_min_freq.replace("cpu0", "cpu" + cpus));
				if (!f.exists()) {
					break;
				}
				cpus++;
			}
			for (int i = 1; i < cpus; i++) {
				commands.add("echo 0 > " + SysUtils.cpu_enable_dir.replace("cpu0", "cpu" + i)
						+ " & " + "chmod 777 " + SysUtils.cpu_enable_dir.replace("cpu0", "cpu" + i) + "\n");
				
			} // end-for: iteration on number of cores/cpus.
			commands.add("exit\n");

			Process p = Runtime.getRuntime().exec(getSUbinaryPath());
			DataOutputStream dos = new DataOutputStream(p.getOutputStream());
			for (String command : commands) {
				dos.writeBytes(command);
				dos.flush();
			}
			int res = p.waitFor();
			if (res == 0) {
				Frequency freq = getCurFreq();
				Log.d("DISABLE CORES", "SUCCESSFUL!");
				Toast.makeText(ctx, resOkMsg, Toast.LENGTH_LONG).show();
				return true;
			} else {
				Toast.makeText(ctx, ctx.getString(resFailedMsg), Toast.LENGTH_LONG).show();
				Log.d(ctx.getString(resFailedMsg), "disableCores");
				return false;
			}
		} catch (Exception ex) {
			Toast.makeText(ctx, "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
			return false;
		}
	}
	
	public static boolean enableCores(Context ctx, int resOkMsg, int resFailedMsg){
		try {
			List<String> commands = new ArrayList<String>();

			int cpus = 0;
			while (true) {
				File f = new File(SysUtils.scaling_min_freq.replace("cpu0", "cpu" + cpus));
				if (!f.exists()) {
					break;
				}
				cpus++;
			}
			for (int i = 1; i < cpus; i++) {
				/*
				 * Prepare permissions so that we can write; governor and frequencies
				 * have different values (root:root vs root:system)
				 */
				commands.add("chmod 4777 " + SysUtils.cpu_enable_dir.replace("cpu0", "cpu" + i) + "\n");
				commands.add("echo 1 > " + SysUtils.cpu_enable_dir.replace("cpu0", "cpu" + i)
						+ "\n");
			} // end-for: iteration on number of cores/cpus.
			
			commands.add("exit\n");

			Process p = Runtime.getRuntime().exec(getSUbinaryPath());
			DataOutputStream dos = new DataOutputStream(p.getOutputStream());
			for (String command : commands) {
				dos.writeBytes(command);
				dos.flush();
			}
			int res = p.waitFor();
			if (res == 0) {
//				String mex = ctx.getString(resOkMsg, getMinFreq(), getMaxFreq(), getGovernor(), getIOScheduler());
//				Toast.makeText(ctx, mex, Toast.LENGTH_LONG).show();
				Log.d("ENABLE CORES", "SUCCESSFUL!");
				Toast.makeText(ctx, resOkMsg, Toast.LENGTH_LONG).show();
				getCurFreq();
				return true;
			} else {
				Toast.makeText(ctx, ctx.getString(resFailedMsg), Toast.LENGTH_LONG).show();
				Log.d(ctx.getString(resFailedMsg), "disableCores");
				return false;
			}
		} catch (Exception ex) {
			Toast.makeText(ctx, "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
			Log.d("Error: " + ex.getMessage(), "disableCores");
			return false;
		}
	}

	
	public static boolean setFrequenciesAndGovernor(String min_freq, String max_freq, String governor,
			String ioscheduler, boolean changePermissions, Context ctx, int resOkMsg, int resFailedMsg) {
		try {
			List<String> commands = new ArrayList<String>();

			if (governor != null || min_freq != null || max_freq != null) {
				/*
				 * Discover the number of cpus and prepare commands for each one. This
				 * applies the same values for every CPU. It could change in the future.
				 * I'm not even sure this is compliant to the specs, but kernel modders
				 * seem to konw better than Linus. :|
				 */
				int cpus = 0;
				while (true) {
					File f = new File(SysUtils.scaling_min_freq.replace("cpu0", "cpu" + cpus));
					if (!f.exists()) {
						break;
					}
					cpus++;
				}
				for (int i = 0; i < cpus; i++) {
					/*
					 * Prepare permissions so that we can write; governor and frequencies
					 * have different values (root:root vs root:system)
					 */
					commands.add("chmod 0644 " + SysUtils.scaling_governor.replace("cpu0", "cpu" + i) + "\n");
					commands.add("chmod 0664 " + SysUtils.scaling_min_freq.replace("cpu0", "cpu" + i) + "\n");
					commands.add("chmod 0664 " + SysUtils.scaling_max_freq.replace("cpu0", "cpu" + i) + "\n");

					if (governor != null) {
						commands.add("echo " + governor + " > " + SysUtils.scaling_governor.replace("cpu0", "cpu" + i) + "\n");
					}

					if (min_freq != null && max_freq != null) {
						String[] available_frequencies = getAvailableFrequencies();
						String lowest_min_freq = available_frequencies[0];

						/*
						 * The first two command assures that the following two commands
						 * will be successful (if max > min).
						 */
						commands.add("echo " + lowest_min_freq + " > " + SysUtils.scaling_min_freq.replace("cpu0", "cpu" + i)
								+ "\n");
						commands.add("echo " + max_freq + " > " + SysUtils.scaling_max_freq.replace("cpu0", "cpu" + i) + "\n");
						commands.add("echo " + min_freq + " > " + SysUtils.scaling_min_freq.replace("cpu0", "cpu" + i) + "\n");
					}
					if (changePermissions) {
						commands.add("chmod 0444 " + SysUtils.scaling_governor.replace("cpu0", "cpu" + i) + "\n");
						commands.add("chmod 0444 " + SysUtils.scaling_min_freq.replace("cpu0", "cpu" + i) + "\n");
						commands.add("chmod 0444 " + SysUtils.scaling_max_freq.replace("cpu0", "cpu" + i) + "\n");
					}
				} // end-for: iteration on number of cores/cpus.
			}

			if (ioscheduler != null) {
				/* Add a line for each applicable block device */
				File sysBlock = new File("/sys/block");
				File[] blockDevices = sysBlock.listFiles();
				for (File blockDevice : blockDevices) {
					if (blockDevice.isDirectory() && !blockDevice.getName().matches(RE_FAKE_BLKDEV)) {
						File queueScheduler = new File(blockDevice, "queue/scheduler");
						if (queueScheduler.exists()) {
							commands.add("chmod 0644 " + queueScheduler.getAbsolutePath() + "\n");
							commands.add("echo " + ioscheduler + " > " + queueScheduler.getAbsolutePath() + "\n");
							if (changePermissions) {
								commands.add("chmod 0444 " + queueScheduler.getAbsolutePath() + "\n");
							}
						}
					}
				}
			}

			commands.add("exit\n");

			Process p = Runtime.getRuntime().exec(getSUbinaryPath());
			DataOutputStream dos = new DataOutputStream(p.getOutputStream());
			for (String command : commands) {
				dos.writeBytes(command);
				dos.flush();
			}
			int res = p.waitFor();
			if (res == 0) {
				String mex = ctx.getString(resOkMsg, getMinFreq(), getMaxFreq(), getGovernor(), getIOScheduler());
				//Toast.makeText(ctx, mex, Toast.LENGTH_LONG).show();
				return true;
			} else {
				Toast.makeText(ctx, ctx.getString(resFailedMsg), Toast.LENGTH_LONG).show();
				return false;
			}
		} catch (Exception ex) {
			Toast.makeText(ctx, "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
			return false;
		}
	}

}
