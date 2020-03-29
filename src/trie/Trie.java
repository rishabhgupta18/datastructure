package trie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Trie {

	static class Node {

		char data;
		Map<Character, Node> childs;
		boolean isEnd;
		Set<String> words;

		public Node(char data) {
			this.data = data;
			this.childs = new HashMap<>(26);
			this.words = new HashSet<>();
		}

		@Override
		public String toString() {
			return "{" + data + "}";
		}

	}

	Node root;

	public Trie() {
		root = new Node('\0');
	}

	public void addAll(List<String> s) {
		for (String _s : s)
			add(_s);
	}

	public void add(String s) {

		char[] c = s.toCharArray();
		if (c.length == 0)
			throw new IllegalArgumentException("string is empty");
		Node current = root;
		Node node = current.childs.get(c[0]);
		if (node == null) {
			node = new Node(c[0]);
			current.childs.put(c[0], node);
		}
		current = node;
		for (int i = 1; i < c.length; i++) {
			Node n = current.childs.get(c[i]);
			if (n == null) {
				n = new Node(c[i]);
				current.childs.put(c[i], n);
			}
			current = n;
		}
		current.isEnd = true;
		node.words.add(s);
	}

	public Set<String> get(char c) {
		return root.childs.get(c) == null ? null : root.childs.get(c).words;
	}

	public boolean contains(String s) {

		char[] c = s.toCharArray();
		if (c.length == 0)
			throw new IllegalArgumentException("string is empty");

		Node current = root;

		for (char _c : c) {

			Node n = current.childs.get(_c);
			if (n == null)
				return false;

			current = n;
		}

		return current.isEnd;
	}

	public Set<String> subSquence(String s, int beginIndex) {
		return subSquence(s.toCharArray(), beginIndex);
	}

	public Set<String> subSquence(char[] c, int beginIndex) {

		Set<String> words = new HashSet<>(c.length);
		StringBuffer b = new StringBuffer();
		Node current = root;

		for (int i = beginIndex; i < c.length; i++) {
			Node n = current.childs.get(c[i]);
			if (n == null)
				break;
			b.append(n.data);
			if (n.isEnd) {
				words.add(b.toString());
			}
			current = n;
		}
		return words;
	}

	/**
	 * @param s = catsanddog
	 * @return [cats and dog, cat sand dog]
	 */
	public List<String> splitAndCombinations(String s) {
		List<String> resp = new ArrayList<>(s.length());
		Map<Integer, Set<String>> cache = new HashMap<>(s.length());
		return splitAndCombinations(s.toCharArray(), 0, new LinkedHashSet<>(s.length()), resp, cache,
				new Boolean(false));
	}

	// To maintain duplicates
	// To optimize add and remove operation
	static class Element {
		String s;

		public Element(String s) {
			this.s = s;
		}

		@Override
		public String toString() {
			return s;
		}
	}

	private List<String> splitAndCombinations(char[] ch, int i, Set<Element> l, List<String> resp,
			Map<Integer, Set<String>> cache, Boolean exitFlag) {

		if (ch.length == 0)
			return resp;

		if (i >= ch.length) {
			StringBuffer b = new StringBuffer();
			for (Element _e : l) {
				b.append(_e.s + " ");
			}
			b.setLength(b.length() - 1);
			resp.add(b.toString());
			return resp;
		}

		Set<String> words = cache.getOrDefault(i, null);
		if (words == null) {
			words = subSquence(ch, i);
			// check if no word in Trie
			// Then the dictionary doesn't have all words
			if (words.size() == 0) {
				exitFlag = true;
				return resp;
			}
			cache.put(i, words);
		}

		for (String s : words) {

			Element e = new Element(s);
			l.add(e);
			splitAndCombinations(ch, i + s.length(), l, resp, cache, exitFlag);
			if (exitFlag)
				return resp;
			l.remove(e);
		}

		return resp;
	}

	public void print() {
		Node n = root;
		print(n, new StringBuffer());
	}

	private void print(Node node, StringBuffer s) {

		if (node == null)
			return;
		if (node.isEnd)
			System.out.println(s.toString());

		Iterator<Character> keys = node.childs.keySet().iterator();
		while (keys.hasNext()) {

			char c = keys.next();
			s.append(c);
			print(node.childs.get(c), s);
			s.setLength(s.length() - 1);

		}

	}

}
