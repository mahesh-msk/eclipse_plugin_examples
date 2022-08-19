/*
 * FILE:            PropertyNameRule.java
 *
 * SW-COMPONENT:    com.ancit.favorites
 *
 * DESCRIPTION:     -
 *
 * COPYRIGHT:       © 2015 - 2022 Robert Bosch GmbH
 *
 * The reproduction, distribution and utilization of this file as
 * well as the communication of its contents to others without express
 * authorization is prohibited. Offenders will be held liable for the
 * payment of damages. All rights reserved in the event of the grant
 * of a patent, utility model or design.
 */
package com.ancit.editors.task;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class PropertyNameRule implements IRule {

	private final Token token;

	public PropertyNameRule(Token token) {
		this.token = token;
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		int c = scanner.read();
		int count = 1;

		while (c != ICharacterScanner.EOF) {

			if (c == ':') {
				return token;
			}

			if ('\n' == c || '\r' == c) {
				break;
			}

			count++;
			c = scanner.read();
		}

		// put the scanner back to the original position if no match
		for (int i = 0; i < count; i++) {
			scanner.unread();
		}

		return Token.UNDEFINED;
	}
}