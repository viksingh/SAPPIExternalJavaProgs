

import java.io.BufferedReader;

import java.io.FileReader;

import java.io.FileWriter;

import java.util.StringTokenizer;
import java.util.Vector;

import java.util.Collections;

import java.util.Date;

import java.text.DateFormat;

import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.logging.Logger;

import java.io.File;

import java.io.FilenameFilter;



public class InvoiceExtract {

  static String userName = "";
	static String srcPath = "\\\\vik-test\\transfers\\AXD\\inbound\\europe";
	static String destPath = "\\\\vik-test\\transfers\\AXD\\inbound\\europe\\invoice\\";
	static Logger log = Logger.getLogger("InvoiceExtract");

	public static void main(String[] args) {

		if (args != null && args.length > 0){
			userName = args[0];
			srcPath = "C:\\Documents and Settings\\" + userName + "\\test\\in\\";
			destPath = "C:\\Documents and Settings\\" + userName + "\\test\\out\\";
		}

		log.info("SRC PATH = " + srcPath);
		log.info("DEST PATH = " + destPath);

		// Select all *.invoice files to process
		File files[];
		File path = new File(srcPath);
		InvoiceFilter filter = new InvoiceFilter();
		files = path.listFiles(filter);

		if (files == null  || files.length < 1){
			log.info("No files to send");
			return;
		}

		//for (int i = 0; i < files.length; i++) {
		for (int i = 0; i < 1; i++) {
			InvoiceFile newInvoice = new InvoiceFile(srcPath + "\\" + files[i].getName(),destPath);
			newInvoice.processInvoices();
		}

	}

}


/**
 * Select only in* files.
 * @param dir the directory in which the file was found.
 * @param name the name of the file
 *
 * @return true if and only if the name should be included in the file list;
 *         false otherwise.
 */
class InvoiceFilter implements FilenameFilter {

	public boolean accept(File dir, String name) {
		if (new File(dir, name).isDirectory())
			return false;

		name = name.toLowerCase();
		return name.startsWith("in");
	}

}



