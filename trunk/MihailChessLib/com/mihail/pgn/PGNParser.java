package com.mihail.pgn;

import java.io.IOException;

/* Generated By:JavaCC: Do not edit this line. PGNParser.java */
// package net.radebatz.chess.PGN;
// import java.io.*;
/**
 * A parser for files in PGN format.
 * 
 * @author Martin Rademacher mano@radebatz.net
 * @versionServidor $Id: PGNParser.jj,v 1.4 2002/06/15 09:00:47 radebatz Exp $
 */
public final class PGNParser extends Parser implements PGNParserConstants {
	public static final int NEXT_TOKEN = 1;

	public static final int SKIP_TOKEN = 0;

	public static final int EOF = -1;

	Game partida = new Game();

	public void analizar() throws ParseException {
		while (parse() != -1) {
			;
		}
	}

	private static final String imageOf(Token t) {
		return (null != t ? t.image : "");
	}

	/*
	 * ******************************************************************************
	 * Productions start here!
	 * ******************************************************************************
	 */

	/**
	 * PGN tag. Doesn't allow white space around the brackets. Tags omitting the
	 * symbol tag are regarded as global comments.
	 */
	final public void PGNTag() throws ParseException {
		Token s = null;
		Token t = null;
		Token v = null;
		s = jj_consume_token(LBRACKET);
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case SPACE:
		case SYMBOL:
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case SYMBOL:
				t = jj_consume_token(SYMBOL);
				label_1: while (true) {
					jj_consume_token(SPACE);
					switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
					case SPACE:
						;
						break;
					default:
						jj_la1[0] = jj_gen;
						break label_1;
					}
				}
				break;
			case SPACE:
				label_2: while (true) {
					jj_consume_token(SPACE);
					switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
					case SPACE:
						;
						break;
					default:
						jj_la1[1] = jj_gen;
						break label_2;
					}
				}
				break;
			default:
				jj_la1[2] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			break;
		default:
			jj_la1[3] = jj_gen;
			;
		}
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case STRING:
			v = jj_consume_token(STRING);
			break;
		case LAZYSTRING:
			v = jj_consume_token(LAZYSTRING);
			break;
		default:
			jj_la1[4] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case SPACE:
			jj_consume_token(SPACE);
			break;
		default:
			jj_la1[5] = jj_gen;
			;
		}
		jj_consume_token(RBRACKET);
		/*
		 * PGNTag token = (PGNTag )factory.getPGNToken(PGNTokenFactory.TAG);
		 * token.setKey(imageOf(t));
		 * token.setValue(stripStringQuotes(trimImage(imageOf(v), 1, 1)));
		 * return token;
		 */
		String st = imageOf(v).substring(1, imageOf(v).length() - 1);
		if (imageOf(t).equals("Event")) {
			partida.evento = st;
		} else if (imageOf(t).equals("Site")) {
			partida.sitio = st;
		} else if (imageOf(t).equals("Round")) {
			partida.ronda = st;
		} else if (imageOf(t).equals("Result")) {
			partida.resultado = st;
		} else if (imageOf(t).equals("Date")) {
			partida.fecha = st;
		} else if (imageOf(t).equals("White")) {
			partida.jugadorB = st;
		} else if (imageOf(t).equals("Black")) {
			partida.jugadorN = st;
		} else if (imageOf(t).equals("ECO")) {
			partida.ECO = st;
		} else if (imageOf(t).equals("TimeControl")) {
			partida.controlDeTiempo = st;
		}
	}

	/**
	 * Move number.
	 */
	final public void MoveNumber() throws ParseException {
		Token t;
		t = jj_consume_token(MOVE_NUMBER);

	}

	/**
	 * A move.
	 */
	final public void Move() throws ParseException {
		Token ss = null;
		Token pp = null;
		Token ii = null;
		Token cc = null;
		Token aa = null;
		Token ff = null;
		Token rr = null;
		if (jj_2_3(2)) {
			ff = jj_consume_token(SQUARE);
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case CAPTURE:
			case DASH:
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case CAPTURE:
					aa = jj_consume_token(CAPTURE);
					break;
				case DASH:
					aa = jj_consume_token(DASH);
					break;
				default:
					jj_la1[6] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
				break;
			default:
				jj_la1[7] = jj_gen;
				;
			}
			ss = jj_consume_token(SQUARE);
			if (jj_2_1(2)) {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case PROMOTE:
					pp = jj_consume_token(PROMOTE);
					break;
				default:
					jj_la1[8] = jj_gen;
					;
				}
				ii = jj_consume_token(PIECE_IDENT);
			} else {
				;
			}
			partida.listaMovimientos.append(imageOf(ff) + imageOf(aa)
					+ imageOf(ss) + imageOf(pp) + imageOf(ii) + " ");
			/*
			 * if(metaDataOnly) return null; token.setFrom(imageOf(ff));
			 * token.setTo(imageOf(ss)); token.setCapture(imageOf(aa));
			 * token.setPromotion(imageOf(ii)); // ff, aa, ss Token tmp = (null ==
			 * ff ? (null == aa ? ss : aa) : ff); return token;
			 */

		} else {
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case FILE_NAME:
			case SQUARE:
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case FILE_NAME:
					ff = jj_consume_token(FILE_NAME);
					switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
					case CAPTURE:
						aa = jj_consume_token(CAPTURE);
						break;
					default:
						jj_la1[9] = jj_gen;
						;
					}
					break;
				default:
					jj_la1[10] = jj_gen;
					;
				}
				ss = jj_consume_token(SQUARE);
				if (jj_2_2(2)) {
					switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
					case PROMOTE:
						pp = jj_consume_token(PROMOTE);
						break;
					default:
						jj_la1[11] = jj_gen;
						;
					}
					ii = jj_consume_token(PIECE_IDENT);
				} else {
					;
				}
				/*
				 * if(metaDataOnly) return null; token.setFrom(imageOf(ff));
				 * token.setTo(imageOf(ss)); token.setCapture(imageOf(aa));
				 * token.setPromotion(imageOf(ii)); // ff, aa, ss Token tmp =
				 * (null == ff ? (null == aa ? ss : aa) : ff); return token;
				 */
				partida.listaMovimientos.append(imageOf(ff) + imageOf(aa)
						+ imageOf(ss) + imageOf(pp) + imageOf(ii) + " ");
				break;
			default:
				jj_la1[18] = jj_gen;
				if (jj_2_4(3)) {
					ii = jj_consume_token(PIECE_IDENT);
					ff = jj_consume_token(SQUARE);
					switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
					case CAPTURE:
					case DASH:
						switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
						case CAPTURE:
							aa = jj_consume_token(CAPTURE);
							break;
						case DASH:
							aa = jj_consume_token(DASH);
							break;
						default:
							jj_la1[12] = jj_gen;
							jj_consume_token(-1);
							throw new ParseException();
						}
						break;
					default:
						jj_la1[13] = jj_gen;
						;
					}
					ss = jj_consume_token(SQUARE);
					/*
					 * if(metaDataOnly) return null;
					 * token.setPiece(imageOf(ii)); token.setFrom(imageOf(ff));
					 * token.setTo(imageOf(ss)); token.setCapture(imageOf(aa));
					 * return token;
					 */
					partida.listaMovimientos.append(imageOf(ii) + imageOf(ff)
							+ imageOf(aa) + imageOf(pp) + imageOf(ss) + " ");
				} else {
					switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
					case PIECE_IDENT:
						ii = jj_consume_token(PIECE_IDENT);
						switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
						case FILE_NAME:
							ff = jj_consume_token(FILE_NAME);
							break;
						default:
							jj_la1[14] = jj_gen;
							;
						}
						switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
						case RANK_NAME:
							rr = jj_consume_token(RANK_NAME);
							break;
						default:
							jj_la1[15] = jj_gen;
							;
						}
						switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
						case CAPTURE:
							aa = jj_consume_token(CAPTURE);
							break;
						default:
							jj_la1[16] = jj_gen;
							;
						}
						ss = jj_consume_token(SQUARE);
						/*
						 * if(metaDataOnly) return null;
						 * token.setPiece(imageOf(ii));
						 * token.setFrom(imageOf(ff)); token.setTo(imageOf(ss));
						 * token.setCapture(imageOf(aa)); return token;
						 */
						partida.listaMovimientos.append(imageOf(ii)
								+ imageOf(ff) + imageOf(rr) + imageOf(aa)
								+ imageOf(ss) + " ");
						break;
					case CASTLE_KINGSIDE:
					case CASTLE_QUEENSIDE:
						switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
						case CASTLE_KINGSIDE:
							cc = jj_consume_token(CASTLE_KINGSIDE);
							break;
						case CASTLE_QUEENSIDE:
							cc = jj_consume_token(CASTLE_QUEENSIDE);
							break;
						default:
							jj_la1[17] = jj_gen;
							jj_consume_token(-1);
							throw new ParseException();
						}
						/*
						 * if(metaDataOnly) return null;
						 * token.setCasteling(cc.image); return token;
						 */
						partida.listaMovimientos.append(imageOf(cc) + " ");
						break;
					case BLACK_MOVE:
						cc = jj_consume_token(BLACK_MOVE);

						break;
					default:
						jj_la1[19] = jj_gen;
						jj_consume_token(-1);
						throw new ParseException();
					}
				}
			}
		}
	}

	/**
	 * Move text is a complete move notation.
	 */
	final public void MoveText() throws ParseException {
		Token cc = null;
		Token ss = null;
		Move();
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case CHECK:
		case CHECKMATE:
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case CHECK:
				cc = jj_consume_token(CHECK);
				break;
			case CHECKMATE:
				cc = jj_consume_token(CHECKMATE);
				break;
			default:
				jj_la1[20] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			break;
		default:
			jj_la1[21] = jj_gen;
			;
		}
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case SUFFIX_ANNOTATION:
			ss = jj_consume_token(SUFFIX_ANNOTATION);
			break;
		default:
			jj_la1[22] = jj_gen;
			;
		}
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case PROMOTE:
			jj_consume_token(PROMOTE);
			break;
		default:
			jj_la1[23] = jj_gen;
			;
		}
		/*
		 * if(metaDataOnly) return null; ((PGNMove
		 * )pgnToken).setCheckMate(imageOf(cc)); ((PGNMove
		 * )pgnToken).setSuffixAnnotation(imageOf(ss)); return pgnToken;
		 */
		partida.listaMovimientos
				.deleteCharAt(partida.listaMovimientos.length() - 1);
		partida.listaMovimientos.append(imageOf(cc) + imageOf(ss) + " ");
	}

	/**
	 * Move text is a complete move notation.
	 */
	final public void GameTerminator() throws ParseException {
		Token gg = null;
		gg = jj_consume_token(GAME_TERMINATOR);
		/*
		 * PGNGameTerminator token = (PGNGameTerminator
		 * )factory.getPGNToken(PGNTokenFactory.GAME_TERMINATOR);
		 * token.setTerminator(imageOf(gg)); return token;
		 */
		listaPartidas.add(partida);
		partida = new Game();
	}

	/**
	 * NAG. A numeric annotation glyph; Range: 0 - 255.
	 */
	final public void NAG() throws ParseException {
		Token gg = null;
		gg = jj_consume_token(NAG);

	}

	/**
	 * Tag comment.
	 */
	final public void TagComment() throws ParseException {
		Token cc = null;
		cc = jj_consume_token(COMMENT);

	}

	/**
	 * Move comment.
	 */
	final public void MoveComment() throws ParseException {
		Token ss = null;
		Token cc = null;
		ss = jj_consume_token(LBRACE);
		cc = jj_consume_token(MOVE_COMMENT);

	}

	/**
	 * Recursive annotation variation.
	 */
	final public void RecAnnotation() throws ParseException {
		Token pp = null;
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case LPAREN:
			pp = jj_consume_token(LPAREN);

			break;
		case RPAREN:
			pp = jj_consume_token(RPAREN);

			break;
		default:
			jj_la1[24] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
	}

	/**
	 * Main production.
	 * 
	 * @return 1 for relevant token, 0 for irrelevant token and -1 for EOF.
	 */
	final public int parse() throws ParseException {
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case LBRACKET:
			PGNTag();
			{
				if (true)
					return NEXT_TOKEN;
			}
			break;
		case MOVE_NUMBER:
			MoveNumber();
			{
				if (true)
					return NEXT_TOKEN;
			}
			break;
		case BLACK_MOVE:
		case FILE_NAME:
		case SQUARE:
		case CASTLE_KINGSIDE:
		case CASTLE_QUEENSIDE:
		case PIECE_IDENT:
			MoveText();
			{
				if (true)
					return NEXT_TOKEN;
			}
			break;
		case COMMENT:
			TagComment();
			{
				if (true)
					return NEXT_TOKEN;
			}
			break;
		case LBRACE:
			MoveComment();
			{
				if (true)
					return NEXT_TOKEN;
			}
			break;
		case GAME_TERMINATOR:
			GameTerminator();
			{
				if (true)
					return NEXT_TOKEN;
			}
			break;
		case LPAREN:
		case RPAREN:
			RecAnnotation();
			{
				if (true)
					return NEXT_TOKEN;
			}
			break;
		case NAG:
			NAG();

			break;
		case END_OF_TOKEN:
		case SPACE:
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case SPACE:
				jj_consume_token(SPACE);
				break;
			case END_OF_TOKEN:
				jj_consume_token(END_OF_TOKEN);
				break;
			default:
				jj_la1[25] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			// theToken = null;
			{
				if (true)
					return SKIP_TOKEN;
			}
			break;
		case 0:
			jj_consume_token(0);
			// theToken = null;
			{
				if (true)
					return EOF;
			}
			break;
		default:
			jj_la1[26] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
		throw new Error("Missing return statement in function");
	}

	final private boolean jj_2_1(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_1();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(0, xla);
		}
	}

	final private boolean jj_2_2(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_2();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(1, xla);
		}
	}

	final private boolean jj_2_3(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_3();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(2, xla);
		}
	}

	final private boolean jj_2_4(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_4();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(3, xla);
		}
	}

	final private boolean jj_3_3() {
		if (jj_scan_token(SQUARE))
			return true;
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_3())
			jj_scanpos = xsp;
		if (jj_scan_token(SQUARE))
			return true;
		return false;
	}

	final private boolean jj_3R_4() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_scan_token(18)) {
			jj_scanpos = xsp;
			if (jj_scan_token(19))
				return true;
		}
		return false;
	}

	final private boolean jj_3R_3() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_scan_token(18)) {
			jj_scanpos = xsp;
			if (jj_scan_token(19))
				return true;
		}
		return false;
	}

	final private boolean jj_3_4() {
		if (jj_scan_token(PIECE_IDENT))
			return true;
		if (jj_scan_token(SQUARE))
			return true;
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_4())
			jj_scanpos = xsp;
		if (jj_scan_token(SQUARE))
			return true;
		return false;
	}

	final private boolean jj_3_2() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_scan_token(17))
			jj_scanpos = xsp;
		if (jj_scan_token(PIECE_IDENT))
			return true;
		return false;
	}

	final private boolean jj_3_1() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_scan_token(17))
			jj_scanpos = xsp;
		if (jj_scan_token(PIECE_IDENT))
			return true;
		return false;
	}

	public PGNParserTokenManager token_source;

	SimpleCharStream jj_input_stream;

	public Token token, jj_nt;

	private int jj_ntk;

	private Token jj_scanpos, jj_lastpos;

	private int jj_la;

	public boolean lookingAhead = false;

	private boolean jj_semLA;

	private int jj_gen;

	final private int[] jj_la1 = new int[27];

	static private int[] jj_la1_0;

	static private int[] jj_la1_1;
	static {
		jj_la1_0();
		jj_la1_1();
	}

	private static void jj_la1_0() {
		jj_la1_0 = new int[] { 0x80000000, 0x80000000, 0x80000000, 0x80000000,
				0x0, 0x80000000, 0xc0000, 0xc0000, 0x20000, 0x40000, 0x800,
				0x20000, 0xc0000, 0xc0000, 0x800, 0x1000, 0x40000, 0xc000,
				0x2800, 0x1c400, 0x900000, 0x900000, 0x1000000, 0x20000, 0x30,
				0x80200000, 0xa221eeb9, };
	}

	private static void jj_la1_1() {
		jj_la1_1 = new int[] { 0x0, 0x0, 0x1, 0x1, 0x6, 0x0, 0x0, 0x0, 0x0,
				0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
				0x0, 0x0, 0x0, 0x0, 0x0, 0x20, };
	}

	final private JJCalls[] jj_2_rtns = new JJCalls[4];

	private boolean jj_rescan = false;

	private int jj_gc = 0;

	public PGNParser(java.io.InputStream stream) {
		// super(stream);
		jj_input_stream = new SimpleCharStream(stream, 1, 1);
		token_source = new PGNParserTokenManager(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 27; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	public void ReInit(java.io.InputStream stream) {
		jj_input_stream.ReInit(stream, 1, 1);
		token_source.ReInit(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 27; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	public PGNParser(java.io.Reader stream) {
		jj_input_stream = new SimpleCharStream(stream, 1, 1);
		token_source = new PGNParserTokenManager(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 27; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	public void ReInit(java.io.Reader stream) {
		jj_input_stream.ReInit(stream, 1, 1);
		token_source.ReInit(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 27; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	public PGNParser(PGNParserTokenManager tm) {
		token_source = tm;
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 27; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	public void ReInit(PGNParserTokenManager tm) {
		token_source = tm;
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 27; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	final private Token jj_consume_token(int kind) throws ParseException {
		Token oldToken;
		if ((oldToken = token).next != null)
			token = token.next;
		else
			token = token.next = token_source.getNextToken();
		jj_ntk = -1;
		if (token.kind == kind) {
			jj_gen++;
			if (++jj_gc > 100) {
				jj_gc = 0;
				for (int i = 0; i < jj_2_rtns.length; i++) {
					JJCalls c = jj_2_rtns[i];
					while (c != null) {
						if (c.gen < jj_gen)
							c.first = null;
						c = c.next;
					}
				}
			}
			return token;
		}
		token = oldToken;
		jj_kind = kind;
		throw generateParseException();
	}

	static private final class LookaheadSuccess extends java.lang.Error {
	}

	final private LookaheadSuccess jj_ls = new LookaheadSuccess();

	final private boolean jj_scan_token(int kind) {
		if (jj_scanpos == jj_lastpos) {
			jj_la--;
			if (jj_scanpos.next == null) {
				jj_lastpos = jj_scanpos = jj_scanpos.next = token_source
						.getNextToken();
			} else {
				jj_lastpos = jj_scanpos = jj_scanpos.next;
			}
		} else {
			jj_scanpos = jj_scanpos.next;
		}
		if (jj_rescan) {
			int i = 0;
			Token tok = token;
			while (tok != null && tok != jj_scanpos) {
				i++;
				tok = tok.next;
			}
			if (tok != null)
				jj_add_error_token(kind, i);
		}
		if (jj_scanpos.kind != kind)
			return true;
		if (jj_la == 0 && jj_scanpos == jj_lastpos)
			throw jj_ls;
		return false;
	}

	final public Token getNextToken() {
		if (token.next != null)
			token = token.next;
		else
			token = token.next = token_source.getNextToken();
		jj_ntk = -1;
		jj_gen++;
		return token;
	}

	final public Token getToken(int index) {
		Token t = lookingAhead ? jj_scanpos : token;
		for (int i = 0; i < index; i++) {
			if (t.next != null)
				t = t.next;
			else
				t = t.next = token_source.getNextToken();
		}
		return t;
	}

	final private int jj_ntk() {
		if ((jj_nt = token.next) == null)
			return (jj_ntk = (token.next = token_source.getNextToken()).kind);
		else
			return (jj_ntk = jj_nt.kind);
	}

	private java.util.Vector jj_expentries = new java.util.Vector();

	private int[] jj_expentry;

	private int jj_kind = -1;

	private int[] jj_lasttokens = new int[100];

	private int jj_endpos;

	private void jj_add_error_token(int kind, int pos) {
		if (pos >= 100)
			return;
		if (pos == jj_endpos + 1) {
			jj_lasttokens[jj_endpos++] = kind;
		} else if (jj_endpos != 0) {
			jj_expentry = new int[jj_endpos];
			for (int i = 0; i < jj_endpos; i++) {
				jj_expentry[i] = jj_lasttokens[i];
			}
			boolean exists = false;
			for (java.util.Enumeration e = jj_expentries.elements(); e
					.hasMoreElements();) {
				int[] oldentry = (int[]) (e.nextElement());
				if (oldentry.length == jj_expentry.length) {
					exists = true;
					for (int i = 0; i < jj_expentry.length; i++) {
						if (oldentry[i] != jj_expentry[i]) {
							exists = false;
							break;
						}
					}
					if (exists)
						break;
				}
			}
			if (!exists)
				jj_expentries.addElement(jj_expentry);
			if (pos != 0)
				jj_lasttokens[(jj_endpos = pos) - 1] = kind;
		}
	}

	public ParseException generateParseException() {
		jj_expentries.removeAllElements();
		boolean[] la1tokens = new boolean[40];
		for (int i = 0; i < 40; i++) {
			la1tokens[i] = false;
		}
		if (jj_kind >= 0) {
			la1tokens[jj_kind] = true;
			jj_kind = -1;
		}
		for (int i = 0; i < 27; i++) {
			if (jj_la1[i] == jj_gen) {
				for (int j = 0; j < 32; j++) {
					if ((jj_la1_0[i] & (1 << j)) != 0) {
						la1tokens[j] = true;
					}
					if ((jj_la1_1[i] & (1 << j)) != 0) {
						la1tokens[32 + j] = true;
					}
				}
			}
		}
		for (int i = 0; i < 40; i++) {
			if (la1tokens[i]) {
				jj_expentry = new int[1];
				jj_expentry[0] = i;
				jj_expentries.addElement(jj_expentry);
			}
		}
		jj_endpos = 0;
		jj_rescan_token();
		jj_add_error_token(0, 0);
		int[][] exptokseq = new int[jj_expentries.size()][];
		for (int i = 0; i < jj_expentries.size(); i++) {
			exptokseq[i] = (int[]) jj_expentries.elementAt(i);
		}
		return new ParseException(token, exptokseq, tokenImage);
	}

	final public void enable_tracing() {
	}

	final public void disable_tracing() {
	}

	final private void jj_rescan_token() {
		jj_rescan = true;
		for (int i = 0; i < 4; i++) {
			JJCalls p = jj_2_rtns[i];
			do {
				if (p.gen > jj_gen) {
					jj_la = p.arg;
					jj_lastpos = jj_scanpos = p.first;
					switch (i) {
					case 0:
						jj_3_1();
						break;
					case 1:
						jj_3_2();
						break;
					case 2:
						jj_3_3();
						break;
					case 3:
						jj_3_4();
						break;
					}
				}
				p = p.next;
			} while (p != null);
		}
		jj_rescan = false;
	}

	final private void jj_save(int index, int xla) {
		JJCalls p = jj_2_rtns[index];
		while (p.gen > jj_gen) {
			if (p.next == null) {
				p = p.next = new JJCalls();
				break;
			}
			p = p.next;
		}
		p.gen = jj_gen + xla - jj_la;
		p.first = token;
		p.arg = xla;
	}

	static final class JJCalls {
		int gen;

		Token first;

		int arg;

		JJCalls next;
	}

	public void guardarArchivo(String archivo) throws IOException {
		java.io.FileWriter f = new java.io.FileWriter(archivo);

		for (int i = 0; i < listaPartidas.size(); i++) {
			Game p = listaPartidas.get(i);
			PGNParser.guardarPartida(archivo, p);
		}
	}

	public static void guardarPartida(String archivo, Game p)
			throws IOException {
		java.io.FileWriter f = new java.io.FileWriter(archivo, true);

		/*
		 * Los siete campos obligatorios son: Event Site Date Round White Black
		 * Result
		 */
		if (p.evento != null)
			f.write("[Event \"" + p.evento + "\"]\n");
		else
			f.write("[Event \"?\"]\n");

		if (p.sitio != null)
			f.write("[Site \"" + p.sitio + "\"]\n");
		else
			f.write("[Site \"Spain\"]\n");

		if (p.fecha != null)
			f.write("[Date \"" + p.fecha + "\"]\n");
		else
			f.write("[Date \"11.09.1984\"]\n");

		if (p.ronda != null)
			f.write("[Round \"" + p.ronda + "\"]\n");
		else
			f.write("[Round \"?\"]\n");

		if (p.jugadorB != null)
			f.write("[White \"" + p.jugadorB + "\"]\n");
		else
			f.write("[White \"white\"]\n");

		if (p.jugadorN != null)
			f.write("[Black \"" + p.jugadorN + "\"]\n");
		else
			f.write("[Black \"black\"]\n");

		if (p.resultado != null)
			f.write("[Result \"" + p.resultado + "\"]\n");
		else
			f.write("[Result \"*\"]\n");

		// Campos opcionales
		if (p.ECO != null)
			f.write("[ECO \"" + p.ECO + "\"]\n");
		if (p.controlDeTiempo != null)
			f.write("[TimeControl \"" + p.controlDeTiempo + "\"]\n");
		if (p.elos != null) {
			// if(p.elos[0] !=null && p.elos[0].)
			f.write("[WhiteElo \"" + p.elos[0] + "\"]\n");
			f.write("[BlackElo \"" + p.elos[1] + "\"]\n");
		}

		f.write("\n");

		int contador = 1;
		String[] mov = p.listaMovimientos.toString().split(" ");
		for (int j = 0; j < mov.length; j += 2) {
			f.write(String.valueOf(contador) + ".");
			f.write(mov[j] + " ");
			if (j + 1 < mov.length)
				f.write(mov[j + 1] + " ");
			contador++;
			if (contador % 5 == 0)
				f.write("\n");
		}

		if (p.resultado != null)
			f.write(p.resultado);
		else
			f.write("*");

		f.write("\n\n");

		f.close();
	}
}