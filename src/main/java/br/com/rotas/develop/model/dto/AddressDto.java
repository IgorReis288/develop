package br.com.rotas.develop.model.dto;

public class AddressDto implements Comparable<AddressDto> {

	private String addressOne;
	private String addressTwo;
	private Double distance;
	
	public String getAddressOne() {
		return addressOne;
	}
	public void setAddressOne(String addressOne) {
		this.addressOne = addressOne;
	}
	public String getAddressTwo() {
		return addressTwo;
	}
	public void setAddressTwo(String addressTwo) {
		this.addressTwo = addressTwo;
	}
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}

	
	@Override
	public int compareTo(AddressDto dto) {
		if (this.getDistance() == null || dto.getDistance() == null) {
			return 0;
		}
	    return this.getDistance().compareTo(dto.getDistance());
	  }
}
