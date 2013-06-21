import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Invoice Container for Lines and totals
 * @author mcarthur
 *
 */
public class InvoiceFile {

  private ArrayList<InvoiceLine> invoiceLines = new ArrayList<InvoiceLine>();
	private String vendor;	// = s1.substring(1, 11);
	private String hdate;	// = s1.substring(11,17);
	private String company;	// = s1.substring(17,27);
	private String destPath;
	static Logger log = Logger.getLogger("InvoiceExtract");
	String lineEnd = System.getProperty("line.separator");
    /**
	 * default constructor must not be used as a file must be imported in order to build the object
	 */
	private InvoiceFile(){};

	/**
	 * Public constructor to build the invoice object to report on...
	 * @param filePath
	 * @param InvoiceLine
	 */
	public InvoiceFile(String filePath, String destPathInbound){

		this.destPath = destPathInbound;
		//open the file
		FileReader fr;
		try {
			fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);

			String line;

			// Loop through lines of the file, storing the file content
			while ((line = br.readLine()) != null) {
				log.info(line);
				if (line.substring(0, 1).equals("H")){
					this.vendor 	= line.substring(1, 11);
					this.hdate 		= line.substring(11,17);
					this.company 	= line.substring(17,27);
				}else if (line.substring(0, 1).equals("D")){
					invoiceLines.add(new InvoiceLine(line));
				}
			}
			fr.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//read each line into an invoiceLine Object and add it to the array
	}

	/**
	 * Matched invoices for a given invoice number
	 * @param invoiceNumber
	 * @return
	 */
	public ArrayList<InvoiceLine> getMatches(String invoiceNumber){
		ArrayList<InvoiceLine> matchedLines = new ArrayList<InvoiceLine>();
		for (InvoiceLine line:this.invoiceLines){
			if (line.invoiceNumber.equalsIgnoreCase(invoiceNumber)){
				matchedLines.add(line);
			}
		}
		return matchedLines;
	}

	/**
	 * Total number of lines on invoice file
	 * @return
	 */
	public int getTotalInvoiceLines(){
		return invoiceLines.size();
	}

	/**
	 * Private to remove duplicate articles per invoice and consolidate figures add quantity and value
	 */
	private void consolidateInvoice(){
		//remove any duplicate invoice lines by article by
		int itr = 0;
		for (String invoiceNumber:this.getUniqueInvoiceList()){
			itr++;
			//output             //remove dup        //invoicelist
			try{
				this.outputInvoice(this.removeDuplicates(this.getMatches(invoiceNumber)),itr);
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Loop through Lines in each output invoice and see if there are duplicate materials
	 * If there are then add the quantity and number
	 * @param singleOutputInvoice
	 * @return
	 */
	private ArrayList<InvoiceLine> removeDuplicates(ArrayList<InvoiceLine> singleOutputInvoice) {

		ArrayList<InvoiceLine> newList = new ArrayList<InvoiceLine>();
		//we need to consolidate the invoice lines if they have the same PO number, PO item and material
		//... add money and number

		//check through looking for duplicate PO number, PO item and material
		for (InvoiceLine line:singleOutputInvoice){
			boolean matched = false;
			int value = 0;
			int qty = 0;
			int indexOfMatch = 0;
			int itr = 0;
			for (InvoiceLine newListLine:newList){
				if (newListLine.getPurchaseOrderNumber().equalsIgnoreCase(line.getPurchaseOrderNumber())
					&& newListLine.getItemNumber().equalsIgnoreCase(line.getItemNumber())
					&& newListLine.getMaterial().equalsIgnoreCase(line.getMaterial())){
					value = newListLine.getValue();
					qty = newListLine.getQuantity();
					//System.out.println("MATCHED");
					matched = true;
					indexOfMatch = itr;
				}
				itr++;
			}

			if (matched){
				//System.out.println("VALUE = " + value + ",  LINE VALUE = " + line.getValue());
				newList.get(indexOfMatch).setValue(line.getValue() + value);
				newList.get(indexOfMatch).setQuantity(line.getQuantity() + qty);

			}else
				newList.add(line);
		}
		return newList;
	}

	/**
	 * Loop through invoices and find a unique list of numbers
	 */
	private ArrayList<String> getUniqueInvoiceList() {
		ArrayList<String> totalList = new ArrayList<String>();

		//for each invoice build a total list on invoice Numbers
		for (InvoiceLine line:invoiceLines){
			totalList.add(line.getInvoiceNumber());
		}

		//now pass the ArrayList into a HASHSET to make it unique
		Set set = new HashSet(totalList);

		//now pass back to a ArrayList for easy use by the calling code
		return new ArrayList<String>(set);
	}

	/**
	 * private to output invoice lines to file
	 * @throws IOException
	 */
	private void outputInvoice(ArrayList<InvoiceLine> lines, int itr) throws IOException{
		//set up output stream
		int total = 0;

		DateFormat dateFormat = new SimpleDateFormat("yyMMdd'_'HHmmss'_'");
		String fNam = dateFormat.format(new Date()) + lines.get(0).getInvoiceNumber() + "_" + itr +  ".invoice";
		//log.info("File: " + fNam);


		FileWriter fOut = new FileWriter(destPath + fNam);

		//output header
		fOut.write("H" + vendor + hdate + company + lineEnd);

		//output lines
		for (InvoiceLine line:lines){
			fOut.write(line.outputLine());
			total+=line.value;
		}

		//output trailer
		fOut.write("T" + vendor + String.format("%04d", lines.size()) + String.format("%015d",total) + lineEnd);
		fOut.close();
	}

	/**
	 * Public method to process invoices and output files
	 */
	public void processInvoices(){
		this.consolidateInvoice();
	}

}
