/*
 * FILE:            TodoPropertiesContentAssistProcessor.java
 *
 * SW-COMPONENT:    com.ancit.editors
 *
 * DESCRIPTION:     -
 *
 * COPYRIGHT:       � 2015 - 2022 Robert Bosch GmbH
 *
 * The reproduction, distribution and utilization of this file as
 * well as the communication of its contents to others without express
 * authorization is prohibited. Offenders will be held liable for the
 * payment of damages. All rights reserved in the event of the grant
 * of a patent, utility model or design.
 */
package com.ancit.editors.task;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

public class TodoPropertiesContentAssistProcessor implements IContentAssistProcessor {

	// public as used later by other code
	public static final List<String> PROPOSALS = Arrays.asList("ID:", "Summary:", "Description:", "Done:", "Duedate:",
			"Dependent:");

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {

		IDocument document = viewer.getDocument();

		try {
			int lineOfOffset = document.getLineOfOffset(offset);
			int lineOffset = document.getLineOffset(lineOfOffset);

			// do not show any content assist in case the offset is not at the
			// beginning of a line
			if (offset != lineOffset) {
				return new ICompletionProposal[0];
			}
		} catch (BadLocationException e) {
			// ignore here and just continue
		}

		return PROPOSALS.stream().filter(proposal -> !viewer.getDocument().get().contains(proposal))
				.map(proposal -> new CompletionProposal(proposal, offset, 0, proposal.length()))
				.toArray(ICompletionProposal[]::new);
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return null;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

}