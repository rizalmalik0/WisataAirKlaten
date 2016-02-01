package malik.wisataairklaten.api;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import malik.wisataairklaten.model.JSONData;
import malik.wisataairklaten.model.JSONPesan;

public class DataDeserializer<T> implements JsonDeserializer<T> {

	@Override
	public T deserialize(JsonElement json, Type tipe,
			JsonDeserializationContext context) throws JsonParseException {
		JsonElement data = null;
		if (tipe.equals(JSONData.class) || tipe.equals(JSONPesan.class)) {
			data = json.getAsJsonObject();
		} else {
			data = json.getAsJsonObject().get("data");
		}

		return new Gson().fromJson(data, tipe);
	}

}
