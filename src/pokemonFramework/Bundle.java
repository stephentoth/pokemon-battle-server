package pokemonFramework;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Bundle implements Serializable {

	private static final long serialVersionUID = -7206046496756732915L;

	private int type;

	public static List<Profile> profiles = new ArrayList<Profile>();

	private Party Party0;
	private Move Move0;

	private Party Party1;
	private Move Move1;

	public Bundle(Profile profile0) {
		type = 0;
		profiles.add(profile0);
	}
	
	public Bundle(Profile profile0, Party party0) {
		type = 1;
		profiles.add(profile0);
		Party0 = party0;
	}

	public Bundle(Profile profile0, Party party0, Move move0) {
		type = 2;
		profiles.add(profile0);
		Party0 = party0;
		Move0 = move0;
	}

	public Bundle(Profile profile0, Party party0, Move move0, Profile profile1, Party party1, Move move1) {
		type = 3;
		profiles.add(profile0);
		profiles.add(profile1);
		Party0 = party0;
		Move0 = move0;
		Party1 = party1;
		Move1 = move1;
	}

	/*
	 * public Bundle(Profile[] profiles) { type = 3; for (int i = 0; i <
	 * profiles.length; i++) { Bundle.profiles.add(i, profiles[i]); } }
	 */

	public Bundle(ArrayList<Profile> profiles) {
		type = 4;
		Bundle.profiles = profiles;
	}

	public Bundle(Profile profile0, Party party0, Move move0, Profile profile1, Party party1, Move move1, Profile profile2, Profile profile3, Profile profile4, Profile profile5, Profile profile6, Profile profile7) {
		type = 7;
		Party0 = party0;
		Move0 = move0;
		Party1 = party1;
		Move1 = move1;
		profiles.add(profile0);
		profiles.add(profile1);
		profiles.add(profile2);
		profiles.add(profile3);
		profiles.add(profile4);
		profiles.add(profile5);
		profiles.add(profile6);
		profiles.add(profile7);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public Profile getSenderProfile() {
		return profiles.get(0);
	}

	public List<Profile> getProfiles() {
		return profiles;
	}

	/*
	 * public void setProfiles(Profile[] profiles) { for (int i = 0; i <
	 * profiles.length; i++) { Bundle.profiles.add(i, profiles[i]); } }
	 */

	public void setProfiles(ArrayList<Profile> profiles) {
		Bundle.profiles = profiles;
	}

	public Party getParty0() {
		return Party0;
	}

	public void setParty0(Party party0) {
		Party0 = party0;
	}

	public Move getMove0() {
		return Move0;
	}

	public void setMove0(Move move0) {
		Move0 = move0;
	}

	public Party getParty1() {
		return Party1;
	}

	public void setParty1(Party party1) {
		Party1 = party1;
	}

	public Move getMove1() {
		return Move1;
	}

	public void setMove1(Move move1) {
		Move1 = move1;
	}

}