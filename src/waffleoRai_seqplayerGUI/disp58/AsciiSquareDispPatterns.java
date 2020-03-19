package waffleoRai_seqplayerGUI.disp58;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AsciiSquareDispPatterns {
	
	public static final String[] P_BOX = {"11111", "10001", "10001", "10001", "10001", "10001", "10001", "11111"};
	
	public static final String[] P_SPACE = {"00000", "00000", "00000", "00000", "00000", "00000", "00000", "00000"};
	public static final String[] P_EXCLAIM = {"00000", "00100", "00100", "00100", "00100", "00000", "00100", "00000"};
	public static final String[] P_DBLQUOTE = {"00000", "01010", "01010", "01010", "00000", "00000", "00000", "00000"};
	public static final String[] P_POUNDSIGN = {"01010", "01010", "11111", "01010", "01010", "11111", "01010", "01010"};
	public static final String[] P_DOLLARSIGN = {"00100", "01110", "01000", "00100", "00010", "01110", "00100", "00000"};
	public static final String[] P_PERCENT = {"00000", "11001", "11010", "00100", "00100", "01011", "10011", "00000"};
	public static final String[] P_AMPERSAND = {"00000", "01110", "10000", "01000", "01101", "10010", "10001", "01110"};
	public static final String[] P_QUOTE = {"00100", "00100", "00100", "00000", "00000", "00000", "00000", "00000"};
	public static final String[] P_PARENLEFT = {"00001", "00010", "00100", "00100", "00100", "00100", "00010", "00001"};
	public static final String[] P_PARENRIGHT = {"10000", "01000", "00100", "00100", "00100", "00100", "01000", "10000"};
	public static final String[] P_ASTERISK = {"00100", "01110", "01010", "00000", "00000", "00000", "00000", "00000"};
	public static final String[] P_PLUS = {"00000", "00100", "00100", "11111", "00100", "00100", "00000", "00000"};
	public static final String[] P_SLASH = {"00001", "00010", "00010", "00100", "00100", "01000", "01000", "10000"};
	public static final String[] P_QUESTIONMARK = {"01110", "10001", "00001", "00110", "00100", "00100", "00000", "00100"};
	
	public static final String[] P_COMMA = {"00000", "00000", "00000", "00000", "00000", "00100", "00100", "01000"};
	public static final String[] P_DASH = {"00000", "00000", "00000", "11111", "00000", "00000", "00000", "00000"};
	public static final String[] P_PERIOD = {"00000", "00000", "00000", "00000", "00000", "00000", "00100", "00000"};
	public static final String[] P_COLON = {"00000", "00000", "00100", "00000", "00000", "00100", "00000", "00000"};
	public static final String[] P_SEMICOLON = {"00000", "00000", "00100", "00000", "00100", "00100", "01000", "00000"};
	
	public static final String[] P_0 = {"01110", "10001", "10001", "10001", "10001", "10001", "10001", "01110"};
	public static final String[] P_1 = {"00100", "01100", "10100", "00100", "00100", "00100", "00100", "11111"};
	public static final String[] P_2 = {"01110", "10001", "00001", "00010", "00100", "01000", "10000", "11111"};
	public static final String[] P_3 = {"01110", "10001", "00001", "00110", "00110", "00001", "10001", "01110"};
	public static final String[] P_4 = {"00110", "01010", "10010", "10010", "11111", "00010", "00010", "00010"};
	public static final String[] P_5 = {"11111", "10000", "10000", "11110", "00001", "00001", "00001", "11110"};
	public static final String[] P_6 = {"00110", "01000", "10000", "10000", "11110", "10001", "10001", "01110"};
	public static final String[] P_7 = {"11111", "00001", "00010", "00100", "01000", "01000", "01000", "01000"};
	public static final String[] P_8 = {"01110", "10001", "10001", "01110", "10001", "10001", "10001", "01110"};
	public static final String[] P_9 = {"01110", "10001", "10001", "01111", "00001", "00010", "00100", "01000"};
	
	public static final String[] P_A = {"01110", "10001", "10001", "10001", "11111", "10001", "10001", "10001"};
	public static final String[] P_B = {"11110", "10001", "10001", "11110", "10001", "10001", "10001", "11110"};
	public static final String[] P_C = {"00111", "01000", "10000", "10000", "10000", "10000", "01000", "00111"};
	public static final String[] P_D = {"11100", "10010", "10001", "10001", "10001", "10001", "10010", "11100"};
	public static final String[] P_E = {"11111", "10000", "10000", "11100", "10000", "10000", "10000", "11111"};
	public static final String[] P_F = {"11111", "10000", "10000", "11100", "10000", "10000", "10000", "10000"};
	public static final String[] P_G = {"01111", "10000", "10000", "10000", "10111", "10001", "10001", "01110"};
	public static final String[] P_H = {"10001", "10001", "10001", "11111", "10001", "10001", "10001", "10001"};
	public static final String[] P_I = {"11111", "00100", "00100", "00100", "00100", "00100", "00100", "11111"};
	public static final String[] P_J = {"11111", "00010", "00010", "00010", "00010", "00010", "00100", "11000"};
	public static final String[] P_K = {"10001", "10001", "10010", "11100", "10010", "10001", "10001", "10001"};
	public static final String[] P_L = {"10000", "10000", "10000", "10000", "10000", "10000", "10000", "11111"};
	public static final String[] P_M = {"10001", "11011", "10101", "10001", "10001", "10001", "10001", "10001"};
	public static final String[] P_N = {"10001", "11001", "11001", "10101", "10101", "10011", "10011", "10001"};
	public static final String[] P_O = {"01110", "10001", "10001", "10001", "10001", "10001", "10001", "01110"};
	public static final String[] P_P = {"11110", "10001", "10001", "10001", "11110", "10000", "10000", "10000"};
	public static final String[] P_Q = {"01110", "10001", "10001", "10001", "10001", "10101", "01110", "00001"};
	public static final String[] P_R = {"11110", "10001", "10001", "10010", "11100", "10010", "10001", "10001"};
	public static final String[] P_S = {"01111", "10000", "10000", "01110", "00001", "00001", "00001", "11110"};
	public static final String[] P_T = {"11111", "00100", "00100", "00100", "00100", "00100", "00100", "00100"};
	public static final String[] P_U = {"10001", "10001", "10001", "10001", "10001", "10001", "10001", "01110"};
	public static final String[] P_V = {"10001", "10001", "10001", "10001", "10001", "01010", "00100", "00100"};
	public static final String[] P_W = {"10001", "10001", "10001", "10001", "10001", "10101", "11011", "10001"};
	public static final String[] P_X = {"10001", "10001", "01010", "00100", "00100", "01010", "10001", "10001"};
	public static final String[] P_Y = {"10001", "10001", "10001", "01010", "00100", "00100", "00100", "00100"};
	public static final String[] P_Z = {"11111", "00001", "00010", "00100", "00100", "01000", "10000", "11111"};
	
	public static final String[] P_a = {"00000", "01110", "10001", "00001", "01111", "10001", "10001", "01111"};
	public static final String[] P_b = {"10000", "10000", "10000", "10000", "11110", "10001", "10001", "11110"};
	public static final String[] P_c = {"00000", "00000", "00000", "01110", "10000", "10000", "10000", "01110"};
	public static final String[] P_d = {"00001", "00001", "00001", "00001", "01111", "10001", "10001", "01111"};
	public static final String[] P_e = {"00000", "00000", "00000", "01110", "10001", "11111", "10000", "01111"};
	public static final String[] P_f = {"00011", "00100", "00100", "11111", "00100", "00100", "00100", "00100"};
	public static final String[] P_g = {"00000", "01111", "10001", "10001", "01111", "00001", "00001", "01110"};
	public static final String[] P_h = {"10000", "10000", "10000", "10000", "11110", "10001", "10001", "10001"};
	public static final String[] P_i = {"00000", "00000", "00100", "00000", "01100", "00100", "00100", "01110"};
	public static final String[] P_j = {"00000", "00100", "00000", "01100", "00100", "00100", "00100", "01000"};
	public static final String[] P_k = {"00000", "10000", "00000", "10010", "10100", "11000", "10010", "10010"};
	public static final String[] P_l = {"01100", "00100", "00100", "00100", "00100", "00100", "00100", "01110"};
	public static final String[] P_m = {"00000", "00000", "00000", "00000", "11011", "10101", "10001", "10001"};
	public static final String[] P_n = {"00000", "00000", "00000", "00000", "11110", "10001", "10001", "10001"};
	public static final String[] P_o = {"00000", "00000", "00000", "01110", "10001", "10001", "10001", "01110"};
	public static final String[] P_p = {"00000", "01110", "10001", "10001", "11110", "10000", "10000", "10000"};
	public static final String[] P_q = {"00000", "01110", "10001", "10001", "11110", "00001", "00001", "00001"};
	public static final String[] P_r = {"00000", "00000", "00000", "00000", "11100", "10010", "10000", "10000"};
	public static final String[] P_s = {"00000", "00000", "00000", "00110", "01000", "00100", "00010", "01100"};
	
	private static Map<Character, boolean[][]> map;
	
	private static boolean[][] toBoolMatrix(String[] strmatrix)
	{
		int height = strmatrix.length;
		int width = strmatrix[0].length();
		
		boolean[][] bmx = new boolean[width][height];
		for(int i = 0; i < height; i++)
		{
			String s = strmatrix[i];
			for(int j = 0; j < width; j++)
			{
				bmx[j][i] = (s.charAt(j) != '0');
			}
		}
		
		return bmx;
	}
	
	private static void buildMap()
	{
		map = new ConcurrentHashMap<Character, boolean[][]>();
		
		boolean[][] boxchar = toBoolMatrix(P_BOX);
		
		map.put(' ', toBoolMatrix(P_SPACE));
		map.put('!', toBoolMatrix(P_EXCLAIM));
		map.put('\"', toBoolMatrix(P_DBLQUOTE));
		map.put('#', toBoolMatrix(P_POUNDSIGN));
		map.put('$', toBoolMatrix(P_DOLLARSIGN));
		map.put('%', toBoolMatrix(P_PERCENT));
		map.put('&', toBoolMatrix(P_AMPERSAND));
		map.put('\'', toBoolMatrix(P_QUOTE));
		map.put('(', toBoolMatrix(P_PARENLEFT));
		map.put(')', toBoolMatrix(P_PARENRIGHT));
		map.put('*', toBoolMatrix(P_ASTERISK));
		map.put('+', toBoolMatrix(P_PLUS));
		map.put(',', toBoolMatrix(P_COMMA));
		map.put('-', toBoolMatrix(P_DASH));
		map.put('.', toBoolMatrix(P_PERIOD));
		map.put('/', toBoolMatrix(P_SLASH));
		
		map.put('0', toBoolMatrix(P_0));
		map.put('1', toBoolMatrix(P_1));
		map.put('2', toBoolMatrix(P_2));
		map.put('3', toBoolMatrix(P_3));
		map.put('4', toBoolMatrix(P_4));
		map.put('5', toBoolMatrix(P_5));
		map.put('6', toBoolMatrix(P_6));
		map.put('7', toBoolMatrix(P_7));
		map.put('8', toBoolMatrix(P_8));
		map.put('9', toBoolMatrix(P_9));
		
		map.put(':', toBoolMatrix(P_COLON));
		map.put(';', toBoolMatrix(P_SEMICOLON));
		map.put('<', boxchar);
		map.put('=', boxchar);
		map.put('>', boxchar);
		map.put('?', toBoolMatrix(P_QUESTIONMARK));
		map.put('@', boxchar);
		
		map.put('A', toBoolMatrix(P_A));
		map.put('B', toBoolMatrix(P_B));
		map.put('C', toBoolMatrix(P_C));
		map.put('D', toBoolMatrix(P_D));
		map.put('E', toBoolMatrix(P_E));
		map.put('F', toBoolMatrix(P_F));
		map.put('G', toBoolMatrix(P_G));
		map.put('H', toBoolMatrix(P_H));
		map.put('I', toBoolMatrix(P_I));
		map.put('J', toBoolMatrix(P_J));
		map.put('K', toBoolMatrix(P_K));
		map.put('L', toBoolMatrix(P_L));
		map.put('M', toBoolMatrix(P_M));
		map.put('N', toBoolMatrix(P_N));
		map.put('O', toBoolMatrix(P_O));
		map.put('P', toBoolMatrix(P_P));
		map.put('Q', toBoolMatrix(P_Q));
		map.put('R', toBoolMatrix(P_R));
		map.put('S', toBoolMatrix(P_S));
		map.put('T', toBoolMatrix(P_T));
		map.put('U', toBoolMatrix(P_U));
		map.put('V', toBoolMatrix(P_V));
		map.put('W', toBoolMatrix(P_W));
		map.put('X', toBoolMatrix(P_X));
		map.put('Y', toBoolMatrix(P_Y));
		map.put('Z', toBoolMatrix(P_Z));
		
		map.put('[', boxchar);
		map.put('\\', boxchar);
		map.put(']', boxchar);
		map.put('^', boxchar);
		map.put('_', boxchar);
		map.put('`', boxchar);
		
		map.put('a', toBoolMatrix(P_a));
		map.put('b', toBoolMatrix(P_b));
		map.put('c', toBoolMatrix(P_c));
		map.put('d', toBoolMatrix(P_d));
		map.put('e', toBoolMatrix(P_e));
		map.put('f', toBoolMatrix(P_f));
		map.put('g', toBoolMatrix(P_g));
		map.put('h', toBoolMatrix(P_h));
		map.put('i', toBoolMatrix(P_i));
		map.put('j', toBoolMatrix(P_j));
		map.put('k', toBoolMatrix(P_k));
		map.put('l', toBoolMatrix(P_l));
		map.put('m', toBoolMatrix(P_m));
		map.put('n', toBoolMatrix(P_n));
		map.put('o', toBoolMatrix(P_o));
		map.put('p', toBoolMatrix(P_p));
		map.put('q', toBoolMatrix(P_q));
		map.put('r', toBoolMatrix(P_r));
		map.put('s', toBoolMatrix(P_s));
		map.put('t', toBoolMatrix(P_T));
		map.put('u', toBoolMatrix(P_U));
		map.put('v', toBoolMatrix(P_V));
		map.put('w', toBoolMatrix(P_W));
		map.put('x', toBoolMatrix(P_X));
		map.put('y', toBoolMatrix(P_Y));
		map.put('z', toBoolMatrix(P_Z));
		
		map.put('{', boxchar);
		map.put('|', boxchar);
		map.put('}', boxchar);
		map.put('~', boxchar);
		
	}
	
	public static boolean[][] getPatternFor(char c)
	{
		if(map == null) buildMap();
		boolean[][] mx = map.get(c);
		if(mx == null) mx = toBoolMatrix(P_BOX);
		return mx;
	}

}
