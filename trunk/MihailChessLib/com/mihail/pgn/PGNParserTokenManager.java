package com.mihail.pgn;

/* Generated By:JavaCC: Do not edit this line. PGNParserTokenManager.java */

public final class PGNParserTokenManager implements PGNParserConstants {
	public java.io.PrintStream debugStream = System.out;

	public void setDebugStream(java.io.PrintStream ds) {
		debugStream = ds;
	}

	private final int jjStopStringLiteralDfa_1(int pos, long active0) {
		switch (pos) {
		default:
			return -1;
		}
	}

	private final int jjStartNfa_1(int pos, long active0) {
		return jjMoveNfa_1(jjStopStringLiteralDfa_1(pos, active0), pos + 1);
	}

	private final int jjStopAtPos(int pos, int kind) {
		jjmatchedKind = kind;
		jjmatchedPos = pos;
		return pos + 1;
	}

	private final int jjStartNfaWithStates_1(int pos, int kind, int state) {
		jjmatchedKind = kind;
		jjmatchedPos = pos;
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			return pos + 1;
		}
		return jjMoveNfa_1(state, pos + 1);
	}

	private final int jjMoveStringLiteralDfa0_1() {
		switch (curChar) {
		case 40:
			return jjStopAtPos(0, 4);
		case 41:
			return jjStopAtPos(0, 5);
		case 43:
			return jjStartNfaWithStates_1(0, 20, 10);
		case 45:
			return jjStopAtPos(0, 19);
		case 46:
			return jjStartNfaWithStates_1(0, 8, 4);
		case 61:
			return jjStopAtPos(0, 17);
		case 91:
			return jjStopAtPos(0, 3);
		case 120:
			return jjStopAtPos(0, 18);
		case 123:
			return jjStopAtPos(0, 7);
		case 125:
			return jjStopAtPos(0, 6);
		default:
			return jjMoveNfa_1(0, 0);
		}
	}

	private final void jjCheckNAdd(int state) {
		if (jjrounds[state] != jjround) {
			jjstateSet[jjnewStateCnt++] = state;
			jjrounds[state] = jjround;
		}
	}

	private final void jjAddStates(int start, int end) {
		do {
			jjstateSet[jjnewStateCnt++] = jjnextStates[start];
		} while (start++ != end);
	}

	private final void jjCheckNAddTwoStates(int state1, int state2) {
		jjCheckNAdd(state1);
		jjCheckNAdd(state2);
	}

	private final void jjCheckNAddStates(int start, int end) {
		do {
			jjCheckNAdd(jjnextStates[start]);
		} while (start++ != end);
	}

	private final void jjCheckNAddStates(int start) {
		jjCheckNAdd(jjnextStates[start]);
		jjCheckNAdd(jjnextStates[start + 1]);
	}

	private final int jjMoveNfa_1(int startState, int curPos) {
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 72;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0x3fe000000000000L & l) != 0L)
							jjCheckNAddTwoStates(1, 2);
						else if ((0x800002000000000L & l) != 0L) {
							if (kind > 35)
								kind = 35;
						} else if (curChar == 10) {
							if (kind > 21)
								kind = 21;
						} else if (curChar == 13)
							jjAddStates(0, 1);
						else if (curChar == 48)
							jjAddStates(2, 4);
						else if (curChar == 33)
							jjCheckNAddTwoStates(40, 41);
						else if (curChar == 42) {
							if (kind > 29)
								kind = 29;
						} else if (curChar == 36)
							jjstateSet[jjnewStateCnt++] = 14;
						else if (curChar == 43)
							jjstateSet[jjnewStateCnt++] = 10;
						else if (curChar == 35) {
							if (kind > 23)
								kind = 23;
						} else if (curChar == 32) {
							if (kind > 21)
								kind = 21;
							jjCheckNAdd(8);
						} else if (curChar == 46)
							jjstateSet[jjnewStateCnt++] = 4;
						else if (curChar == 63)
							jjCheckNAddTwoStates(40, 41);
						if ((0x1fe000000000000L & l) != 0L) {
							if (kind > 12)
								kind = 12;
						} else if ((0x8000000200000000L & l) != 0L) {
							if (kind > 24)
								kind = 24;
						}
						if (curChar == 49)
							jjAddStates(5, 6);
						break;
					case 1:
						if ((0x3ff000000000000L & l) != 0L)
							jjCheckNAddTwoStates(1, 2);
						break;
					case 2:
						if (curChar == 46 && kind > 9)
							kind = 9;
						break;
					case 3:
						if (curChar == 46)
							jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 4:
						if (curChar != 46)
							break;
						if (kind > 10)
							kind = 10;
						jjstateSet[jjnewStateCnt++] = 5;
						break;
					case 5:
						if (curChar == 46 && kind > 10)
							kind = 10;
						break;
					case 6:
						if ((0x1fe000000000000L & l) != 0L && kind > 12)
							kind = 12;
						break;
					case 8:
						if (curChar != 32)
							break;
						kind = 21;
						jjCheckNAdd(8);
						break;
					case 9:
						if (curChar == 35)
							kind = 23;
						break;
					case 10:
						if (curChar == 43 && kind > 23)
							kind = 23;
						break;
					case 11:
						if (curChar == 43)
							jjstateSet[jjnewStateCnt++] = 10;
						break;
					case 12:
						if ((0x8000000200000000L & l) != 0L && kind > 24)
							kind = 24;
						break;
					case 13:
						if (curChar == 36)
							jjstateSet[jjnewStateCnt++] = 14;
						break;
					case 14:
						if ((0x3fe000000000000L & l) == 0L)
							break;
						if (kind > 25)
							kind = 25;
						jjstateSet[jjnewStateCnt++] = 15;
						break;
					case 15:
						if ((0x3ff000000000000L & l) == 0L)
							break;
						if (kind > 25)
							kind = 25;
						jjstateSet[jjnewStateCnt++] = 16;
						break;
					case 16:
						if ((0x3ff000000000000L & l) != 0L && kind > 25)
							kind = 25;
						break;
					case 17:
						if (curChar == 42)
							kind = 29;
						break;
					case 29:
						if ((0x800002000000000L & l) != 0L)
							kind = 35;
						break;
					case 30:
						if (curChar == 49)
							jjAddStates(5, 6);
						break;
					case 31:
						if (curChar == 48 && kind > 29)
							kind = 29;
						break;
					case 32:
						if (curChar == 45)
							jjstateSet[jjnewStateCnt++] = 31;
						break;
					case 33:
						if (curChar == 50 && kind > 29)
							kind = 29;
						break;
					case 34:
						if (curChar == 47)
							jjstateSet[jjnewStateCnt++] = 33;
						break;
					case 35:
						if (curChar == 49)
							jjstateSet[jjnewStateCnt++] = 34;
						break;
					case 36:
						if (curChar == 45)
							jjstateSet[jjnewStateCnt++] = 35;
						break;
					case 37:
						if (curChar == 50)
							jjstateSet[jjnewStateCnt++] = 36;
						break;
					case 38:
						if (curChar == 47)
							jjstateSet[jjnewStateCnt++] = 37;
						break;
					case 39:
						if (curChar == 33)
							jjCheckNAddTwoStates(40, 41);
						break;
					case 40:
						if (curChar == 33 && kind > 24)
							kind = 24;
						break;
					case 41:
						if (curChar == 63 && kind > 24)
							kind = 24;
						break;
					case 42:
						if (curChar == 63)
							jjCheckNAddTwoStates(40, 41);
						break;
					case 44:
						if ((0x1fe000000000000L & l) != 0L && kind > 13)
							kind = 13;
						break;
					case 47:
						if (curChar == 45)
							jjstateSet[jjnewStateCnt++] = 46;
						break;
					case 49:
						if (curChar == 45)
							jjstateSet[jjnewStateCnt++] = 48;
						break;
					case 51:
						if (curChar == 45)
							jjstateSet[jjnewStateCnt++] = 50;
						break;
					case 52:
						if (curChar == 48)
							jjAddStates(2, 4);
						break;
					case 53:
						if (curChar == 48 && kind > 14)
							kind = 14;
						break;
					case 54:
						if (curChar == 45)
							jjstateSet[jjnewStateCnt++] = 53;
						break;
					case 55:
						if (curChar == 48 && kind > 15)
							kind = 15;
						break;
					case 56:
						if (curChar == 45)
							jjstateSet[jjnewStateCnt++] = 55;
						break;
					case 57:
						if (curChar == 48)
							jjstateSet[jjnewStateCnt++] = 56;
						break;
					case 58:
						if (curChar == 45)
							jjstateSet[jjnewStateCnt++] = 57;
						break;
					case 59:
						if (curChar == 49 && kind > 29)
							kind = 29;
						break;
					case 60:
						if (curChar == 45)
							jjstateSet[jjnewStateCnt++] = 59;
						break;
					case 63:
						if (curChar == 45)
							jjstateSet[jjnewStateCnt++] = 62;
						break;
					case 65:
						if (curChar == 45)
							jjstateSet[jjnewStateCnt++] = 64;
						break;
					case 67:
						if (curChar == 45)
							jjstateSet[jjnewStateCnt++] = 66;
						break;
					case 68:
						if (curChar == 13)
							jjAddStates(0, 1);
						break;
					case 69:
						if (curChar == 10 && kind > 21)
							kind = 21;
						break;
					case 70:
						if (curChar == 10 && kind > 22)
							kind = 22;
						break;
					case 71:
						if (curChar == 10 && kind > 21)
							kind = 21;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0x1fe00000000L & l) != 0L) {
							if (kind > 11)
								kind = 11;
							jjstateSet[jjnewStateCnt++] = 44;
						} else if ((0x64804L & l) != 0L) {
							if (kind > 16)
								kind = 16;
						} else if (curChar == 111)
							jjAddStates(7, 8);
						else if (curChar == 79)
							jjAddStates(9, 10);
						if ((0x1000000010L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 27;
						else if ((0x4000000040000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 23;
						break;
					case 7:
						if ((0x64804L & l) != 0L && kind > 16)
							kind = 16;
						break;
					case 18:
						if ((0x8000000080000L & l) != 0L && kind > 29)
							kind = 29;
						break;
					case 19:
						if ((0x400000004000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 18;
						break;
					case 20:
						if ((0x8000000080L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 19;
						break;
					case 21:
						if ((0x20000000200L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 20;
						break;
					case 22:
						if ((0x8000000080000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 21;
						break;
					case 23:
						if ((0x2000000020L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 22;
						break;
					case 24:
						if ((0x4000000040000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 23;
						break;
					case 25:
						if ((0x80000000800000L & l) != 0L)
							kind = 29;
						break;
					case 26:
						if ((0x200000002L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 25;
						break;
					case 27:
						if ((0x4000000040000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 26;
						break;
					case 28:
						if ((0x1000000010L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 27;
						break;
					case 43:
						if ((0x1fe00000000L & l) == 0L)
							break;
						if (kind > 11)
							kind = 11;
						jjstateSet[jjnewStateCnt++] = 44;
						break;
					case 45:
						if (curChar == 79)
							jjAddStates(9, 10);
						break;
					case 46:
						if (curChar == 79 && kind > 14)
							kind = 14;
						break;
					case 48:
						if (curChar == 79 && kind > 15)
							kind = 15;
						break;
					case 50:
						if (curChar == 79)
							jjstateSet[jjnewStateCnt++] = 49;
						break;
					case 61:
						if (curChar == 111)
							jjAddStates(7, 8);
						break;
					case 62:
						if (curChar == 111 && kind > 14)
							kind = 14;
						break;
					case 64:
						if (curChar == 111 && kind > 15)
							kind = 15;
						break;
					case 66:
						if (curChar == 111)
							jjstateSet[jjnewStateCnt++] = 65;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 72 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjStopStringLiteralDfa_0(int pos, long active0) {
		switch (pos) {
		default:
			return -1;
		}
	}

	private final int jjStartNfa_0(int pos, long active0) {
		return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
	}

	private final int jjStartNfaWithStates_0(int pos, int kind, int state) {
		jjmatchedKind = kind;
		jjmatchedPos = pos;
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			return pos + 1;
		}
		return jjMoveNfa_0(state, pos + 1);
	}

	private final int jjMoveStringLiteralDfa0_0() {
		switch (curChar) {
		case 10:
			return jjStopAtPos(0, 2);
		case 13:
			return jjStopAtPos(0, 1);
		default:
			return jjMoveNfa_0(0, 0);
		}
	}

	static final long[] jjbitVec0 = { 0x0L, 0x0L, 0xffffffffffffffffL,
			0xffffffffffffffffL };

	private final int jjMoveNfa_0(int startState, int curPos) {
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 2;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						kind = 38;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0xdfffffffffffffffL & l) != 0L) {
							if (kind > 38)
								kind = 38;
						} else if (curChar == 125) {
							if (kind > 39)
								kind = 39;
						}
						break;
					case 1:
						if (curChar == 125)
							kind = 39;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((jjbitVec0[i2] & l2) != 0L && kind > 38)
							kind = 38;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 2 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjMoveStringLiteralDfa0_3() {
		return jjMoveNfa_3(0, 0);
	}

	private final int jjMoveNfa_3(int startState, int curPos) {
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 3;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 1;
						else if (curChar == 10) {
							if (kind > 37)
								kind = 37;
						}
						break;
					case 1:
						if (curChar == 10 && kind > 37)
							kind = 37;
						break;
					case 2:
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 1;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 3 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjStopStringLiteralDfa_2(int pos, long active0) {
		switch (pos) {
		default:
			return -1;
		}
	}

	private final int jjStartNfa_2(int pos, long active0) {
		return jjMoveNfa_2(jjStopStringLiteralDfa_2(pos, active0), pos + 1);
	}

	private final int jjStartNfaWithStates_2(int pos, int kind, int state) {
		jjmatchedKind = kind;
		jjmatchedPos = pos;
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			return pos + 1;
		}
		return jjMoveNfa_2(state, pos + 1);
	}

	private final int jjMoveStringLiteralDfa0_2() {
		switch (curChar) {
		case 32:
			return jjStopAtPos(0, 31);
		case 93:
			return jjStopAtPos(0, 30);
		default:
			return jjMoveNfa_2(0, 0);
		}
	}

	private final int jjMoveNfa_2(int startState, int curPos) {
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 19;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0x3ff000000000000L & l) != 0L) {
							if (kind > 32)
								kind = 32;
							jjCheckNAdd(1);
						} else if (curChar == 34)
							jjCheckNAddStates(11, 16);
						break;
					case 1:
						if ((0x27ff280800000000L & l) == 0L)
							break;
						if (kind > 32)
							kind = 32;
						jjCheckNAdd(1);
						break;
					case 2:
						if (curChar == 34)
							jjCheckNAddStates(11, 16);
						break;
					case 3:
						if ((0xfffffffbffffdbffL & l) != 0L)
							jjCheckNAddStates(17, 19);
						break;
					case 5:
						if ((0x8400000000L & l) != 0L)
							jjCheckNAddStates(17, 19);
						break;
					case 6:
						if (curChar == 34 && kind > 33)
							kind = 33;
						break;
					case 7:
						if ((0xff000000000000L & l) != 0L)
							jjCheckNAddStates(20, 23);
						break;
					case 8:
						if ((0xff000000000000L & l) != 0L)
							jjCheckNAddStates(17, 19);
						break;
					case 9:
						if ((0xf000000000000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 10;
						break;
					case 10:
						if ((0xff000000000000L & l) != 0L)
							jjCheckNAdd(8);
						break;
					case 11:
						if ((0xffffffffffffdbffL & l) != 0L)
							jjCheckNAddStates(24, 26);
						break;
					case 13:
						if ((0x8400000000L & l) != 0L)
							jjCheckNAddStates(24, 26);
						break;
					case 14:
						if (curChar == 34 && kind > 34)
							kind = 34;
						break;
					case 15:
						if ((0xff000000000000L & l) != 0L)
							jjCheckNAddStates(27, 30);
						break;
					case 16:
						if ((0xff000000000000L & l) != 0L)
							jjCheckNAddStates(24, 26);
						break;
					case 17:
						if ((0xf000000000000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 18;
						break;
					case 18:
						if ((0xff000000000000L & l) != 0L)
							jjCheckNAdd(16);
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0x7fffffe07fffffeL & l) == 0L)
							break;
						if (kind > 32)
							kind = 32;
						jjCheckNAdd(1);
						break;
					case 1:
						if ((0x7fffffe87fffffeL & l) == 0L)
							break;
						if (kind > 32)
							kind = 32;
						jjCheckNAdd(1);
						break;
					case 3:
						if ((0xffffffffefffffffL & l) != 0L)
							jjCheckNAddStates(17, 19);
						break;
					case 4:
						if (curChar == 92)
							jjAddStates(31, 33);
						break;
					case 5:
						if ((0x14404410000000L & l) != 0L)
							jjCheckNAddStates(17, 19);
						break;
					case 11:
						jjCheckNAddStates(24, 26);
						break;
					case 12:
						if (curChar == 92)
							jjAddStates(34, 36);
						break;
					case 13:
						if ((0x14404410000000L & l) != 0L)
							jjCheckNAddStates(24, 26);
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 3:
						if ((jjbitVec0[i2] & l2) != 0L)
							jjAddStates(17, 19);
						break;
					case 11:
						if ((jjbitVec0[i2] & l2) != 0L)
							jjAddStates(24, 26);
						break;
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 19 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	static final int[] jjnextStates = { 69, 70, 54, 58, 60, 32, 38, 63, 67, 47,
			51, 3, 4, 6, 11, 12, 14, 3, 4, 6, 3, 4, 8, 6, 11, 12, 14, 11, 12,
			16, 14, 5, 7, 9, 13, 15, 17, };

	public static final String[] jjstrLiteralImages = { null, null, null,
			"\133", "\50", "\51", "\175", "\173", "\56", null, null, null,
			null, null, null, null, null, "\75", "\170", "\55", "\53", null,
			null, null, null, null, null, null, null, null, "\135", "\40",
			null, null, null, null, null, null, null, null, };

	public static final String[] lexStateNames = { "IN_MOVE_COMMENT",
			"DEFAULT", "IN_PGN_TAG", "IN_COMMENT", };

	public static final int[] jjnewLexState = { -1, -1, -1, 2, -1, -1, -1, 0,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, 1, -1, -1, -1, -1, 3, -1, 1, -1, 1, };

	static final long[] jjtoToken = { 0xa7e3fffff9L, };

	static final long[] jjtoSkip = { 0x6L, };

	static final long[] jjtoMore = { 0x5800000000L, };

	protected SimpleCharStream input_stream;

	private final int[] jjrounds = new int[72];

	private final int[] jjstateSet = new int[144];

	protected char curChar;

	public PGNParserTokenManager(SimpleCharStream stream) {
		if (SimpleCharStream.staticFlag)
			throw new Error(
					"ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
		input_stream = stream;
	}

	public PGNParserTokenManager(SimpleCharStream stream, int lexState) {
		this(stream);
		SwitchTo(lexState);
	}

	public void ReInit(SimpleCharStream stream) {
		jjmatchedPos = jjnewStateCnt = 0;
		curLexState = defaultLexState;
		input_stream = stream;
		ReInitRounds();
	}

	private final void ReInitRounds() {
		int i;
		jjround = 0x80000001;
		for (i = 72; i-- > 0;)
			jjrounds[i] = 0x80000000;
	}

	public void ReInit(SimpleCharStream stream, int lexState) {
		ReInit(stream);
		SwitchTo(lexState);
	}

	public void SwitchTo(int lexState) {
		if (lexState >= 4 || lexState < 0)
			throw new TokenMgrError("Error: Ignoring invalid lexical state : "
					+ lexState + ". State unchanged.",
					TokenMgrError.INVALID_LEXICAL_STATE);
		else
			curLexState = lexState;
	}

	protected Token jjFillToken() {
		Token t = Token.newToken(jjmatchedKind);
		t.kind = jjmatchedKind;
		String im = jjstrLiteralImages[jjmatchedKind];
		t.image = (im == null) ? input_stream.GetImage() : im;
		return t;
	}

	int curLexState = 1;

	int defaultLexState = 1;

	int jjnewStateCnt;

	int jjround;

	int jjmatchedPos;

	int jjmatchedKind;

	public Token getNextToken() {
		int kind;
		Token specialToken = null;
		Token matchedToken;
		int curPos = 0;

		EOFLoop: for (;;) {
			try {
				curChar = input_stream.BeginToken();
			} catch (java.io.IOException e) {
				jjmatchedKind = 0;
				matchedToken = jjFillToken();
				return matchedToken;
			}

			for (;;) {
				switch (curLexState) {
				case 0:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_0();
					break;
				case 1:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_1();
					break;
				case 2:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_2();
					break;
				case 3:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_3();
					if (jjmatchedPos == 0 && jjmatchedKind > 36) {
						jjmatchedKind = 36;
					}
					break;
				}
				if (jjmatchedKind != 0x7fffffff) {
					if (jjmatchedPos + 1 < curPos)
						input_stream.backup(curPos - jjmatchedPos - 1);
					if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
						matchedToken = jjFillToken();
						if (jjnewLexState[jjmatchedKind] != -1)
							curLexState = jjnewLexState[jjmatchedKind];
						return matchedToken;
					} else if ((jjtoSkip[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
						if (jjnewLexState[jjmatchedKind] != -1)
							curLexState = jjnewLexState[jjmatchedKind];
						continue EOFLoop;
					}
					if (jjnewLexState[jjmatchedKind] != -1)
						curLexState = jjnewLexState[jjmatchedKind];
					curPos = 0;
					jjmatchedKind = 0x7fffffff;
					try {
						curChar = input_stream.readChar();
						continue;
					} catch (java.io.IOException e1) {
					}
				}
				int error_line = input_stream.getEndLine();
				int error_column = input_stream.getEndColumn();
				String error_after = null;
				boolean EOFSeen = false;
				try {
					input_stream.readChar();
					input_stream.backup(1);
				} catch (java.io.IOException e1) {
					EOFSeen = true;
					error_after = curPos <= 1 ? "" : input_stream.GetImage();
					if (curChar == '\n' || curChar == '\r') {
						error_line++;
						error_column = 0;
					} else
						error_column++;
				}
				if (!EOFSeen) {
					input_stream.backup(1);
					error_after = curPos <= 1 ? "" : input_stream.GetImage();
				}
				throw new TokenMgrError(EOFSeen, curLexState, error_line,
						error_column, error_after, curChar,
						TokenMgrError.LEXICAL_ERROR);
			}
		}
	}

}
