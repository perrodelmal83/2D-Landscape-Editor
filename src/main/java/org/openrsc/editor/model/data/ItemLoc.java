package org.openrsc.editor.model.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.awt.Point;

@Getter
@Builder(toBuilder = true)
public class ItemLoc {
	private final int amount;
	private final int id;
	private final int respawnTime;
	private final Point location;

	@JsonCreator
	public ItemLoc(
		@JsonProperty("id") int id,
		@JsonProperty("amount") int amount,
		@JsonProperty("respawn") int respawnTime,
		@JsonProperty("location") Point location
	) {
		this.id = id;
		this.location = location;
		this.amount = amount;
		this.respawnTime = respawnTime;
	}
}
