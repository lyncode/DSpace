/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.i18n;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

public class DSpaceXLIFFMessageSource implements
		MessageSource {
	private static Logger log = LogManager.getLogger(DSpaceXLIFFMessageSource.class);
	private static final String DEFAULT_DIR = "config" + File.separator + "i18n";
	
	@Autowired ConfigurationService config;
	private Map<Locale, XLIFF> map;
	
	private String baseDir;
	private String dirSuffix;
	
	public String getBaseDir () {
		if (baseDir == null) {
			baseDir = config.getProperty("i18n.dir");
			if (baseDir == null) baseDir = config.getProperty("dspace.dir") + File.separator + DEFAULT_DIR;
		}
		if (dirSuffix != null)
			return baseDir + File.separator + this.dirSuffix;
		else return baseDir;
	}

	public void setSubDirectory (String val) {
		this.dirSuffix = val;
	}
	
	private File getFile (Locale loc) {
		String filename = "messages_" + loc.getLanguage() + ".xliff";
		return new File(this.getBaseDir(), filename);
	}

	private File getFile (String loc) {
		String filename = "messages_" + loc + ".xliff";
		return new File(this.getBaseDir(), filename);
	}

	private File getFile () {
		String filename = "messages.xliff";
		return new File(this.getBaseDir(), filename);
	}
	
	public void refresh (Locale loc) {
		// File rewritten..
		File f = this.getFile(loc);
		if (!f.exists()) {
			log.error("Unable to find translation file for locale "+loc.getLanguage()+" in "+this.getBaseDir()+" using default one");
		} else {
			InputStream is = null;
			try {
				is = new FileInputStream(f);
				map.put(loc, XLiffUtils.read(is));
				
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
	
	private XLIFF getXliff (Locale loc) {
		if (map == null) map = new TreeMap<Locale, XLIFF>();
		if (!map.containsKey(loc)) {
			File f = this.getFile(loc);
			if (!f.exists()) {
				log.error("Unable to find translation file for locale "+loc.getLanguage()+" in "+this.getBaseDir()+" using default one");
				// Try the default one
				String defaul = config.getProperty("default.language");
				if (defaul != null) {
					f = this.getFile(defaul);
				} else f = this.getFile();
			}
			if (!f.exists()) {
				log.error("Unable to find translation files in "+this.getBaseDir());
				return null;
			}
			else {
				InputStream is = null;
				try {
					is = new FileInputStream(f);
					map.put(loc, XLiffUtils.read(is));
					
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
		return map.get(loc);
	}

	@Override
	public String getMessage(String code, Object[] args, String defaultMessage,
			Locale locale) {
		XLIFF x = this.getXliff(locale);
		if (x == null) return defaultMessage;
		else {
			String cod = x.getTarget(code);
			if (cod == null) return String.format(defaultMessage, args);
			else return String.format(cod, args);
		}
	}

	@Override
	public String getMessage(String code, Object[] args, Locale locale)
			throws NoSuchMessageException {
		XLIFF x = this.getXliff(locale);
		if (x == null) return "";
		else {
			String cod = x.getTarget(code);
			if (cod == null) return "";
			else return String.format(cod, args);
		}
	}

	@Override
	public String getMessage(MessageSourceResolvable resolvable, Locale locale)
			throws NoSuchMessageException {
		for (String code : resolvable.getCodes()) {
			XLIFF x = this.getXliff(locale);
			if (x != null) {
				String cod = x.getTarget(code);
				if (cod != null) return String.format(cod, resolvable.getArguments());
			}
		}
		return resolvable.getDefaultMessage();
	}
}
