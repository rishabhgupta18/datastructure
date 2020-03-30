package run;

import java.util.Arrays;
import java.util.List;

import trie.Trie;

public class TrieMain {

	public static void main(String[] args) {

		Trie t = new Trie();
		t.addAll(Arrays.asList("apple", "pen", "applepen", "pine", "pineapple"));
		System.out.println(t.splitAndCombinations("pineapplepenapple"));

		t = new Trie();
		t.addAll(Arrays.asList("aaaa", "aaa"));
		System.out.println(t.splitAndCombinations("aaaaaaa"));

		t = new Trie();
		t.addAll(Arrays.asList("cat", "cats", "and", "sand", "dog"));
		System.out.println(t.splitAndCombinations("catsanddog"));

		t.addAll(Arrays.asList("a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa", "aaaaaaa", "aaaaaaaa", "aaaaaaaaa",
				"aaaaaaaaaa"));
		List<String> sl = t.splitAndCombinations(
				"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		System.out.println(sl);

	}
}
