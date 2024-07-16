package com.uniroma3.prog.model;

import java.util.Arrays;

import jakarta.persistence.*;
import org.apache.tomcat.util.codec.binary.Base64;

@Entity
public class Image {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Lob
	@Column
	private byte[] imageData;

	@Column(name = "mime_type")
	private String mimeType;
	
	public String generateBase64Image() {
		return Base64.encodeBase64String(this.imageData);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(imageData);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Image other = (Image) obj;
		return Arrays.equals(imageData, other.imageData);
	}

}
