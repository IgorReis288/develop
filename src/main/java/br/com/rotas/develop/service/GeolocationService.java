package br.com.rotas.develop.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;

import br.com.rotas.develop.model.dto.AddressDto;
import br.com.rotas.develop.model.form.AddressForm;

@Service
public class GeolocationService {

	private final String key = "<sua key>";

	public List<AddressForm> findLatLng(String addressInput) throws ApiException, InterruptedException, IOException {
		
		GeoApiContext context = new GeoApiContext.Builder().apiKey(this.key).build();
		GeocodingApiRequest request = GeocodingApi.newRequest(context).address(addressInput);
		
		GeocodingResult[] results =  request.await();
		
		List<AddressForm> addresses = new ArrayList<AddressForm>();
		for(int i = 0; results.length > i; i++){
			GeocodingResult result = results[i];
			AddressForm address = new AddressForm();
			Geometry geometry = result.geometry;
			LatLng location = geometry.location;
			
			address.setAddress(result.formattedAddress);
			address.setLat(location.lat);
			address.setLng(location.lng);
			addresses.add(address);
		}
		
		return addresses;
	}
	
	public List<AddressDto> calculateDistance(List<AddressForm> addresses) {
		
		List<AddressDto> adds = new ArrayList<AddressDto>();
		for(int i = 0; addresses.size() > i; i++){
			AddressForm add = addresses.get(i);
			
			for(int n = 0; addresses.size() > n; n++) {
				if(add.equals(addresses.get(n))) {
					continue;
				}
			
				double x1 = add.getLat();
				double y1 = add.getLng();
				double x2 = addresses.get(n).getLat();
				double y2 = addresses.get(n).getLng();
				Double result = Math.sqrt((Math.pow(x2 - x1, 2)) + (Math.pow(y2 - y1, 2)));
				
				AddressDto dto = new AddressDto();
				dto.setAddressOne(add.getAddress());
				dto.setAddressTwo(addresses.get(n).getAddress());
				dto.setDistance(result);
				
				if(isExist(dto, adds)) {
					continue;
				}
				
				adds.add(dto);
			}						
		}
		return adds;
	}
	
	public boolean isExist(AddressDto dto, List<AddressDto> dtos) {
	
		for(int i = 0; dtos.size() > i; i++) {
			if(dtos.get(i).getDistance().equals(dto.getDistance())) {
				return true;
			}
		}
		return false;
	}
	
	
	public String addRecommendation(List<AddressDto> dto) {
		
		if(dto.size() == 1) {
			String rcm = "A diferença entre os endereços: ";
			for(int i = 0; dto.size() > i; i++) {
				rcm += dto.get(i).getAddressOne() + " e " + 
					   dto.get(i).getAddressTwo() + " é: " +
					   dto.get(i).getDistance() + "; ";
			}
			return rcm;
		}
		
		int rest = dto.size()%2;
		int half = dto.size()/2;
		Collections.sort(dto);
		String rcmNear = "Os endereços mais próximos são: ";
		for(int i = 0; half > i; i++) {
			rcmNear += dto.get(i).getAddressOne() + " e " + 
					   dto.get(i).getAddressTwo() + " com distância de " +
					   dto.get(i).getDistance() + "; ";
		}
			
		Collections.reverse(dto);
		String rcmFar = "Os endereços mais distantes são: ";
		for(int i = 0; (half + rest) > i; i++) {
			rcmFar += dto.get(i).getAddressOne() + " e " + 
					  dto.get(i).getAddressTwo() + " com distância de " +
					  dto.get(i).getDistance() + "; ";	
		}
		return rcmNear + rcmFar;		
	}

}
