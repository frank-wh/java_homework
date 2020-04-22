package calculator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Stack;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculator extends JFrame{
	JTextField t = new JTextField();
	String store = "";
	String ans = "";
	public Calculator(Color fg, Color bg, int width, int height) {
		setSize(width, height);
		Container c = getContentPane();
		c.setBackground(bg);
		c.setForeground(fg);
		
		Font f = new Font("Times New Roman", Font.BOLD, 60);
		Font fscreen = new Font("Times New Roman", Font.BOLD, 100);
        	t.setFont(fscreen);
		t.setBackground(Color.BLACK);
		t.setForeground(Color.WHITE);
		c.add(BorderLayout.NORTH, t);
        
		// set keyboard
		JPanel p = new JPanel();
        	p.setBackground(new Color(50, 50, 50));
        	p.setLayout(new GridLayout(5,5, 4,4));
        	String[] labels = {
        		"+","-","*","/","#",
            		"1","2","3","(",")",
            		"4","5","6","[","]",
            		"7","8","9","{","}",
            		"CR","0","=",".","ANS",
        	};
		for(int i = 0; i < 25; i++){
		    JButton button = new JButton(labels[i]);
		    p.add(button);
		    button.setFont(f);
		    button.addActionListener(new MyListener()); 
		}
		JPanel p2 = new JPanel();
		p2.add(p);
		JTextArea textarea = new JTextArea();
		textarea.setFont(f);
		p2.add(textarea);
		c.add(BorderLayout.CENTER, p2);
		
        	setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private static boolean isOperator(String s) {
		return s.equals("+")||s.equals("-")||s.equals("*")||s.equals("/");
	}
	
	private static boolean isOpeningParen(String s) {
		return s.equals("(")||s.equals("{")||s.equals("[");
	}
	
	private static boolean isClosingParen(String s) {
		return s.equals(")")||s.equals("}")||s.equals("]");
	}
	
	private static boolean hasHigherPrec(String peek, String s) {
		return (peek.equals("*")||peek.equals("/")) && (s.equals("+")||s.equals("-"));
	}
	
	private boolean isParentheses(String str){
		int n = str.length();
		Stack<Character> stack = new Stack<>();
		for(int i = 0; i < n; i++) {
			char c = str.charAt(i);
			if(c == '(' || c == '{' || c == '[')
				stack.push(c);
			else if(c == ')' || c == '}' || c == ']'){
				if(stack.isEmpty())
					return false;
				char top = stack.pop();
				if((top=='('&&c!=')')||(top=='['&&c!=']')||(top=='{'&&c!='}'))
					return false;
			}
		}
		return stack.isEmpty();
	}
	
	private boolean isValidFormula(String s) {
		int end = s.length() - 1;
		if(s.charAt(0)=='+'||s.charAt(0)=='*'||s.charAt(0)=='/')
			return false;
		if(s.charAt(end)=='+'||s.charAt(end)=='-'||s.charAt(end)=='*'||s.charAt(end)=='/')
			return false;
		String[] checkPoint = s.split("[0-9]+");
		for(int i = 0; i < checkPoint.length - 1; i++) {
			if(checkPoint[i].equals(".") && checkPoint[i+1].equals("."))
				return false;
		}
		for(int i = 0; i < s.length() - 1; i++) {
			String index = String.valueOf(s.charAt(i));
			String next = String.valueOf(s.charAt(i+1));
			if(isOperator(index) && (isOperator(next) || isClosingParen(next))) 
				return false;
			else if(isOpeningParen(index) && isOperator(next) && !next.equals("-"))
				return false;
		}
		return true;
	}
	
	private String toStandardFormula(String res) {
		if(res.charAt(0) == '-')
			res = '0' + res;
		for(int i = 0; i < res.length() - 1; i++) {
			if((res.charAt(i)-'0'>=0&&res.charAt(i)-'9'<=0) && res.charAt(i+1)=='(')
				res = res.substring(0, i+1) + "*" + res.substring(i+1);
			else if(res.charAt(i)==')' && (res.charAt(i+1)-'0'>=0&&res.charAt(i+1)-'9'<=0))
				res = res.substring(0, i+1) + "*" + res.substring(i+1);
			else if(res.charAt(i)=='(' && res.charAt(i+1)=='-')
				res = res.substring(0, i+1) + "0" + res.substring(i+1);;
		}
		return res;
	}
	
	private static String perform(String sign, String op2, String op1) {
		String res = "";
		BigDecimal b2 = new BigDecimal(op2);
        	BigDecimal b1 = new BigDecimal(op1);
		if(sign.equals("/") && Double.valueOf(op1)==0.0) return "error!";
		switch(sign) {
			case "+":
				res = b2.add(b1).toString();
				break;
			case "-":
				res = b2.subtract(b1).toString();
				break;
			case "*":
				res = b2.multiply(b1).toString();
				break;
			default:
				double d = b2.divide(b1, 8, RoundingMode.HALF_UP).doubleValue();
				res = d % 1 == 0? Integer.toString((int) d) : String.valueOf(d);
		}
		return res;
	}
	
	private String evaluatePostfix(String[] str){
		Stack<String> S = new Stack<>();
		for(int i = 0; i < str.length; i++) {
			if(str[i] == null) break;
			if(!isOperator(str[i]))
				S.push(str[i]);
			else {
				String op1 = S.pop();
				String op2 = S.pop();
				String res = perform(str[i], op2, op1);
				if(res.equals("error!")) return res;
				S.push(res);
			}
		}
		return S.peek();
	}
	
	private String[] infixToPostfix(String[] str) {
		Stack<String> S = new Stack<>();
		String[] res = new String[str.length];
		int[] prec = new int[str.length];
		int index = 0, level = 0;
		for(int i = 0; i < str.length; i++) {
			if(str[i] == null) break;
			
			if(!isOperator(str[i]) && !isOpeningParen(str[i]) && !isClosingParen(str[i])) {
				res[index] = str[i];
				prec[index++] = level;
			}
			else if(isOperator(str[i])) {
				while(!S.isEmpty() && !isOpeningParen(S.peek()) && hasHigherPrec(S.peek(), str[i])) {
					res[index] = S.pop();
					prec[index++] = level;
				}
				S.push(str[i]);
			}
			else if(isOpeningParen(str[i])) {
				S.push(str[i]);
				level++;
			}
			else {
				String temp = str[i];
				if(temp.equals(")"))
					temp = "(";
				else if(temp.equals("]"))
					temp = "[";
				else
					temp = "{";
				while(!S.isEmpty() && !S.peek().equals(temp)) {
					res[index] = S.pop();
					prec[index++] = level;
				}
				S.pop();
				level--;
			}
		}
		while(!S.isEmpty()) {
			res[index] = S.pop();
			prec[index++] = level;
		}
		index = 0;
		while(res[index+1] != null) {
			if(res[index+1].equals("-") && res[index].equals("-") && prec[index]==prec[index+1])
				res[index] = "+";
			else if(res[index+1].equals("-") && res[index].equals("+") && prec[index]==prec[index+1])
				res[index] = "-";
			else if(res[index+1].equals("/") && res[index].equals("/") && prec[index]==prec[index+1])
				res[index] = "*";
			else if(res[index+1].equals("/") && res[index].equals("*") && prec[index]==prec[index+1])
				res[index] = "/";
			index++;
		}
		return res;
	}
	
	private String[] seperateOperator(String s) {
		String[] arr = new String[20];
		int index = 0, flag = 0;
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c=='+'||c=='-'||c=='*'||c=='/'||c=='('||c==')'||c=='['||c==']'||c=='{'||c=='}') {
				if(flag < i) {
					arr[index++] = s.substring(flag, i);
					arr[index++] = s.substring(i, i + 1);
					flag = i + 1;
				} else {
					arr[index++] = s.substring(i, i + 1);
					flag = i + 1;
				}
			}
			else if(i == s.length() - 1)
				arr[index++] = s.substring(flag, i + 1);
			if(index == arr.length - 1 || index == arr.length) {
				String[] temp = new String[arr.length * 2];
				for(int j = 0; j < arr.length; j++)
					temp[j] = arr[j];
				arr = temp;
			}
		}
		return arr;
	}
	
	public static void main(String[] args) {
		new Calculator(Color.BLACK, Color.GRAY, 800, 600);
	}
	
	class MyListener extends JFrame implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String[] str = {
				"+","-","*","/",
		            	"1","2","3","(",")",
		            	"4","5","6","[","]",
		            	"7","8","9","{","}",
		                    "0",    ".",
			};
		    	String clear = "";
		    	store += e.getActionCommand();
		    	for (String s : str) {
				if (e.getActionCommand().equals(s))
			    		t.setText(store);
		    	}
		    	if(e.getActionCommand().equals("CR")){ // enter "CR" -> clear text field
				store = "";
				t.setText(clear);
		    	}
		    	if(e.getActionCommand().equals("#")){  // enter "#" -> back
				if(!store.equals("") && !store.equals("#"))
					store = store.substring(0, store.length()-2);
				if(store.equals("#"))
					store = "";
				t.setText(store);
		    	}
		    	if(e.getActionCommand().equals("ANS")){ // enter "ANS" -> show last result
				store = "";
				t.setText(ans); 
		    	}
		    	if(e.getActionCommand().equals("=")){  // enter "#" -> calculate the formula
				String res = store.substring(0, store.length()-1);
				store = "";
				if(res.equals("") || !isParentheses(res) || !isValidFormula(res)) {
					ans = "";
					t.setText("ERROR!");
				} else {
					res = toStandardFormula(res);
					String[] arr = seperateOperator(res);
					arr = infixToPostfix(arr);
					ans = evaluatePostfix(arr);
					t.setText(ans);
				}
		    	}
		}
	}
}
