import java.util.logging.Logger;


public class InvoiceLine {

  String 	purchaseOrderNumber;
	String 	invoiceNumber;
	int		value;
	int 	quantity;
	String  itemNumber;
	String 	material;
	String	date;
	String 	uom;
	String 	country;
	String 	currency;
	String 	delivery;
	String 	salesNumber;
	String  lineEnd = System.getProperty("line.separator");

	static Logger log = Logger.getLogger("InvoiceExtract");

	public InvoiceLine(){};

	public InvoiceLine(String totalLine){

		this.purchaseOrderNumber = totalLine.substring(1,26);
		this.salesNumber = totalLine.substring(26,39);
		this.invoiceNumber = totalLine.substring(39,49) ;
		//System.err.println("INVOICE NUMBER = " + invoiceNumber);
		this.date = totalLine.substring(49,55);
		this.country =  totalLine.substring(55,58);
		this.itemNumber = totalLine.substring(58,64);
		this.material = totalLine.substring(64,82);
		//System.err.println("MATERIAL = " + material);
		this.uom = totalLine.substring(82,85);
		this.quantity = Integer.parseInt(totalLine.substring(85,98).trim());
		//System.err.println("QUANTITY = " + quantity);
		this.value = Integer.parseInt(totalLine.substring(98,113).trim());
		//System.err.println("VALUE = " + value);
		this.currency = totalLine.substring(113, 118);
		this.delivery = totalLine.substring(118);
	}

	public String getVendor() {
		return purchaseOrderNumber;
	}

	public void setVendor(String vendor) {
		this.purchaseOrderNumber = vendor;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int newValue) {
		this.value = newValue;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getPurchaseOrderNumber() {
		return purchaseOrderNumber;
	}

	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String outputLine() {
		//fOut.write(s2.substring(66,151) + String.format("%013d",totQty) + String.format("%015d",totAmt) + s2.substring(179) + lineEnd);
		//v2.add(s2.substring(124,130) + "-" + s2.substring(66,151) + String.format("%013d",totQty) + String.format("%015d",totAmt) + s2.substring(179));
		//fOut.write(this.invoiceNumber + " " + this.itemNumber + " " + this.material);
		return "D" + this.purchaseOrderNumber +  this.salesNumber + this.invoiceNumber +  this.date + this.country + this.itemNumber  + this.material +
			   this.uom + String.format("%013d",this.quantity) +
			   String.format("%015d",this.value) + this.currency + this.delivery + lineEnd;
	}

}
