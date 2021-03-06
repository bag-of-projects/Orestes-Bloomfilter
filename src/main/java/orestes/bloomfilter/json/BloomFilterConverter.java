package orestes.bloomfilter.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import orestes.bloomfilter.BloomFilter;
import orestes.bloomfilter.BloomFilter.HashMethod;

import java.util.Base64;
import java.util.BitSet;

public class BloomFilterConverter {
	
	/**
	 * Convert a Bloom filter into a JSON Element.
	 * @param source the Bloom filter to convert
	 * @return the JSON representation of the Bloom filter
	 */
	public static JsonElement toJson(BloomFilter<?> source) {
		JsonObject root = new JsonObject();
		root.addProperty("m", source.getM());
		root.addProperty("k", source.getK());
		root.addProperty("HashMethod", source.getHashMethod().toString());
		root.addProperty("CryptographicHashFunction", source.getCryptographicHashFunctionName());
		byte[] bits = source.getBitSet().toByteArray();
		
		// Encode using Arrays.toString -> [0,16,0,0,32[].
		// root.addProperty("bits", Arrays.toString(bits));
		
		// Encode using base64 -> AAAAAQAAQAAAAAAgA
		root.addProperty("bits", new String(Base64.getEncoder().encode(bits)));
		
		return root;
	}
	
	
	public static BloomFilter<String> fromJson(JsonElement source) {
		return fromJson(source, String.class);
	}
	
	/**
	 * Constructs a Bloom filter from its JSON representation
	 * 
	 * @param source the JSON source
	 * @param type Generic type parameter of the Bloom filter
	 * @return the Bloom filter
	 */
	public static <T> BloomFilter<T> fromJson(JsonElement source, Class<T> type) {
		JsonObject root = source.getAsJsonObject();
		int m = root.get("m").getAsInt();
		int k = root.get("k").getAsInt();
		String hashMethod = root.get("HashMethod").getAsString();
		String hashFunctionName = root.get("CryptographicHashFunction").getAsString();
		byte[] bits = null;
		
		bits = Base64.getDecoder().decode(root.get("bits").getAsString());
		BloomFilter<T> bf = new BloomFilter<T>(BitSet.valueOf(bits), m, k, HashMethod.Cryptographic,
				hashFunctionName);
		bf.setHashMethod(HashMethod.valueOf(hashMethod));
		return bf;
	}
	

}
