/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.i18n;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dspace.services.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import com.lyncode.xliff.XLIFF;
import com.lyncode.xliff.XLiffUtils;

public class DSpaceXLIFFMessageSource implements MessageSource {
	private static Logger log = LogManager.getLogger(DSpaceXLIFFMessageSource.class);
	private static final String DEFAULT_DIR = "config" + File.separator + "i18n";
	private static final String MESSAGE = "messages";
	private static final String EXT = ".xliff";

	@Autowired
	ConfigurationService config;
	private Map<String, XLIFF> map;
	private Map<String, String> localeMap = new TreeMap<String, String>();
	private WatchService watch;
	private String baseDir;

	private String getBaseDir() {
		if (baseDir == null) {
			baseDir = config.getProperty("i18n.dir");
			if (baseDir == null)
				baseDir = config.getProperty("dspace.dir") + File.separator
						+ DEFAULT_DIR;
		}
		String base = baseDir;

		try {
			if (watch == null) {
				watch = FileSystems.getDefault().newWatchService();
				Path dir = FileSystems.getDefault().getPath(base);
				dir.register(watch, StandardWatchEventKinds.ENTRY_MODIFY);
			}
		} catch (IOException e) {
			log.error("Cannot refresh files", e);
		}

		return base;
	}

	private File getFileFrom(String s) {
		return new File(this.getBaseDir(), MESSAGE + s + EXT);
	}

	@SuppressWarnings("unchecked")
	private void look() {
		if (watch != null) {
			WatchKey key = watch.poll();
			while (key != null) {
				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();
					if (kind == StandardWatchEventKinds.OVERFLOW) {
						continue;
					}

					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path filename = ev.context();

					String name = filename.getFileName().toString();
					name = name.replace(MESSAGE, "").replace(EXT, "");
					log.info("File "+filename.toString()+" updated refreshing messages");
					this.refresh(name);
				}
				boolean valid = key.reset();
				if (!valid)
					key = null;
				else
					key = watch.poll();
			}
		}
	}

	private void refresh(String sub) {
		// File rewritten..
		File f = this.getFileFrom(sub);
		if (!f.exists()) {
			log.error("Unable to find translation file for locale " + sub
					+ " in " + this.getBaseDir() + " using default one");
		} else {
			InputStream is = null;
			try {
				is = new FileInputStream(f);
				map.put(sub, XLiffUtils.read(is));
			} catch (Exception e) {
				log.error("Unable to read xliff file", e);
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						log.debug(e.getMessage(), e);
					}
				}
			}
		}
	}

	private String checkFileToString (Locale loc) {
		String result = "_" + loc.getLanguage();
		File f = this.getFileFrom(result);
		if (!f.exists()) {
			log.info("Unable to find translation file ("+f.getAbsolutePath()+") for locale "
					+ loc.getLanguage() + " in " + this.getBaseDir()
					+ " trying configured default one");
			// Try the default one
			String defaul = config.getProperty("default.language");
			if (defaul != null) {
				result = "_" + defaul;
				f = this.getFileFrom(result);
				if (f.exists())
					return result;
				else
					log.info("Unable to find translation file ("+f.getAbsolutePath()+") for locale "
							+ loc.getLanguage() + " in " + this.getBaseDir()
							+ " trying default one");
			} 
			if (!f.exists()) {
				result = "";
				f = this.getFileFrom(result);
				if (f.exists())
					return result;
				else
					log.error("Unable to find translation file ("+f.getAbsolutePath()+") for locale "
							+ loc.getLanguage() + " in " + this.getBaseDir()
							+ " returning null");
			}
		} else
			return result;
		
		return null;
	}
	
	private String localeToString (Locale loc) {
		if (!localeMap.containsKey(loc.getLanguage())) {
			String l = this.checkFileToString(loc);
			if (l != null) localeMap.put(loc.getLanguage(), l);
		}
		return localeMap.get(loc.getLanguage());
	}

	private XLIFF getXliff(Locale loc) {
		if (map == null)
			map = new TreeMap<String, XLIFF>();
		String sub = this.localeToString(loc);
		if (sub == null) {
			log.error("Unable to find translation files in "
					+ this.getBaseDir());
			return null;
		}
		if (!map.containsKey(sub)) {
			File f = this.getFileFrom(sub);
			InputStream is = null;
			try {
				is = new FileInputStream(f);
				XLIFF x = XLiffUtils.read(is);
				if (x != null)
					map.put(sub, x);

			} catch (Exception e) {
				log.error("Unable to read xliff file", e);
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						log.debug(e.getMessage(), e);
					}
				}
			}
		}
		return map.get(sub);
	}

	@Override
	public String getMessage(String code, Object[] args, String defaultMessage,
			Locale locale) {
		this.look(); // Refresh files if updated
		XLIFF x = this.getXliff(locale);
		if (x == null)
			return String.format(defaultMessage, args);
		else {
			String cod = x.getTarget(code);
			if (cod == null)
				return String.format(defaultMessage, args);
			else
				return String.format(cod, args);
		}
	}

	@Override
	public String getMessage(String code, Object[] args, Locale locale)
			throws NoSuchMessageException {
		this.look(); // Refresh files if updated
		XLIFF x = this.getXliff(locale);
		if (x == null)
			return String.format(code, args);
		else {
			String cod = x.getTarget(code);
			if (cod == null)
				return String.format(code, args);
			else
				return String.format(cod, args);
		}
	}

	@Override
	public String getMessage(MessageSourceResolvable resolvable, Locale locale)
			throws NoSuchMessageException {
		this.look(); // Refresh files if updated
		for (String code : resolvable.getCodes()) {
			XLIFF x = this.getXliff(locale);
			if (x != null) {
				String cod = x.getTarget(code);
				if (cod != null)
					return String.format(cod, resolvable.getArguments());
			}
		}
		return String.format(resolvable.getDefaultMessage(),
				resolvable.getArguments());
	}
}
