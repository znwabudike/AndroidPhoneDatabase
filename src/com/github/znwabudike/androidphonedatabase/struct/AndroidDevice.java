package com.github.znwabudike.androidphonedatabase.struct;

public class AndroidDevice {
	
	private String modelNum;
	private String commonName;
	private String carrier;
	private String manufacturer;
	
	int numProps = 4;
	
	public AndroidDevice(){}
	public AndroidDevice(String manufacturer,
			String modelNum, 
			String commonName, 
			String carrier){
		this.manufacturer = manufacturer;
		this.modelNum = modelNum;
		this.commonName = commonName;
		this.carrier = carrier;
	}
	public String getModelNum() {return modelNum;
	}
	public void setModelNum(String modelNum) {this.modelNum = modelNum;
	}
	public String getCommonName() {return commonName;
	}
	public void setCommonName(String commonName) {this.commonName = commonName;
	}
	public String getCarrier() {return carrier;
	}
	public void setCarrier(String carrier) {this.carrier = carrier;
	}
	public String getManufacturer() {return manufacturer;
	}
	public void setManufacturer(String manufacturer){this.manufacturer = manufacturer;
	}
	public int getNumProperties() {	return numProps;
	}
	public void printDevice(){
		System.out.println("=========================");
		System.out.println("Brand: " + manufacturer);
		System.out.println("Model: " + modelNum);
		System.out.println("Common: " + commonName);
		System.out.println("Carrier: " + carrier);
		System.out.println("=========================");
	}
	
	
	
	
	
}
