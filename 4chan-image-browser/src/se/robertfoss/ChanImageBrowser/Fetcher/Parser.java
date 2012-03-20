package se.robertfoss.ChanImageBrowser.Fetcher;

import java.util.ArrayList;
import java.util.HashMap;

import se.robertfoss.ChanImageBrowser.Viewer;

import dk.brics.automaton.*;

public class Parser {

	private static HashMap<String, RunAutomaton> regexAutomatonMap;
	
	static {
		regexAutomatonMap = new HashMap<String, RunAutomaton>();
	}
	
	public static ArrayList<String> parseForStrings(String input, String regex, int resultFromIndex) {
		Viewer.printDebug("Regexp: Starting search for - " + regex);
		
		RunAutomaton automaton = regexAutomatonMap.get(regex);
		if (automaton == null){
			automaton = new RunAutomaton(new RegExp(regex).toAutomaton());
			regexAutomatonMap.put(regex, automaton);
		}
		
		ArrayList<String> matches = new ArrayList<String>();

		if (input != null) {
			AutomatonMatcher automMatcher = automaton.newMatcher(input);

			while (automMatcher.find()) {
				matches.add(automMatcher.group(0).substring(resultFromIndex));
			}
		}
		
		Viewer.printDebug("Regexp: Ending search for - " + regex);
		
		return matches;
	}
	
	
}
