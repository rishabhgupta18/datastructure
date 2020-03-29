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
		// mark the start of the node
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

	public Set<String> find(String s, int start) {
		return find(s.toCharArray(), start);
	}
	
	public Set<String> find(char c){
		return root.childs.get(c).words;
	}

	public Set<String> find(char[] c, int start) {

		Set<String> words = new HashSet<>(c.length);
		StringBuffer b = new StringBuffer();
		Node current = root;

		for (int i = start; i < c.length; i++) {
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
	

	public List<String> findAll(String s) {
		List<String> resp = new ArrayList<>(s.length());
		Map<Integer, Set<String>> dp = new HashMap<>(s.length());
		return findAll(s.toCharArray(), 0, new LinkedHashSet<>(s.length()), resp, dp, new Boolean(false));
	}

	// To maintain duplicates
	// To optimize add and remove operation
	private List<String> findAll(char[] ch, int i, Set<Element> l, List<String> resp, Map<Integer, Set<String>> dp,
			Boolean exitFlag) {

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

		Set<String> words = dp.getOrDefault(i, null);
		if (words == null) {
			words = find(ch, i);
			// check if no word in Trie
			// Then the dictionary doesn't have all words
			if (words.size() == 0) {
				exitFlag = true;
				return resp;
			}
			dp.put(i, words);
		}

		for (String s : words) {

			Element e = new Element(s);
			l.add(e);
			findAll(ch, i + s.length(), l, resp, dp, exitFlag);
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
		if(node.isEnd)
			System.out.println(s.toString());

		Iterator<Character> keys = node.childs.keySet().iterator();
		while (keys.hasNext()) {

			char c = keys.next();
			s.append(c);
			print(node.childs.get(c), s);
			s.setLength(s.length() - 1);

		}

	}

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

}
