package trie;

import java.util.ArrayList;
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

	// To get all words by starting character
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

	public Set<String> subSequence(String s, int beginIndex) {
		return subSequence(s.toCharArray(), beginIndex);
	}

	// returns all matching sequence from begin index
	// for a string - catsanddog and beginIndex = 0
	// returns [cat, cats]
	public Set<String> subSequence(char[] c, int beginIndex) {

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

	// To maintain duplicates in set
	// To optimize add and remove operation by using Set
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

	/**
	 * @param s = catsanddog
	 * @return [cats and dog, cat sand dog]
	 */
	public List<String> splitAndCombinations(String s) {
		Map<Integer, Set<Element>> cache = new HashMap<>(s.length());
		splitAndCombinations(s.toCharArray(), 0, new LinkedHashSet<>(s.length()), cache);
		List<String> resp = new ArrayList<>();
		Set<Element> se = cache.get(0);
		for (Element e : se) {
			resp.add(e.s);
		}

		return resp;
	}

	// O(n2) in worst case
	private void splitAndCombinations(char[] ch, int i, Set<Element> l, Map<Integer, Set<Element>> cache) {

		if (ch.length == 0 || i >= ch.length) {
			if (i == ch.length) {
				Set<Element> se = new LinkedHashSet<>();
				se.add(new Element(""));
				cache.put(i, se);
			}

			return;
		}
		if (cache.containsKey(i))
			return;

		Set<String> words = subSequence(ch, i);
		Set<Element> se = new LinkedHashSet<>();
		for (String s : words) {
			// Check if next combination exists in cache
			Set<Element> set = cache.get(i + s.length());
			if (set != null) {
				// compute current combination
				for (Element _e : set) {
					Element ne = new Element(_e.s == "" ? s : s + " " + _e.s);
					se.add(ne);
				}

			} else {

				Element e = new Element(s);
				l.add(e);
				splitAndCombinations(ch, i + s.length(), l, cache);
				// Since we compute the next combination
				// now compute current combination
				set = cache.get(i + s.length());
				if (set != null) {
					for (Element _e : set) {
						Element ne = new Element(_e.s == "" ? s : s + " " + _e.s);
						se.add(ne);
					}

				}
				l.remove(e);
			}
		}
		cache.put(i, se);

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
