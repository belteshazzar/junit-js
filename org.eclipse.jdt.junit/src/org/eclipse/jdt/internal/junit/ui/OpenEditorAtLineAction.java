/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Sebastian Davids: sdavids@gmx.de bug 37333 Failure Trace cannot
 * 			navigate to non-public class in CU throwing Exception
 *******************************************************************************/
package org.eclipse.jdt.internal.junit.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.core.resources.IFile;

import org.eclipse.jface.operation.IRunnableWithProgress;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import org.eclipse.ui.PlatformUI;

import org.eclipse.ui.texteditor.ITextEditor;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Open a test in the Java editor and reveal a given line
 */
public class OpenEditorAtLineAction extends OpenEditorAction {

	private int fLineNumber;

	public OpenEditorAtLineAction(TestRunnerViewPart testRunner, String className, int line) {
		super(testRunner, className);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJUnitHelpContextIds.OPENEDITORATLINE_ACTION);
		fLineNumber= line;
	}

	@Override
	protected void reveal(ITextEditor textEditor) {
		if (fLineNumber >= 0) {
			try {
				IDocument document= textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
				textEditor.selectAndReveal(document.getLineOffset(fLineNumber-1), document.getLineLength(fLineNumber-1));
			} catch (BadLocationException x) {
				// marker refers to invalid text position -> do nothing
			}
		}
	}

	@Override
	protected IJavaElement findElement(IJavaProject project, String className) throws CoreException {
		return findType(project, className);
	}

	@Override
	protected IFile findFile(final IJavaProject project) {
		try {
			final IFile[] result = { null };
			PlatformUI.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						String classname = fTestRunner.getFailureTrace().getFailedTest().getClassName();
						String filename = classname.substring(0, classname.indexOf(":")); //$NON-NLS-1$
						result[0] = internalFindFile(project, filename, new HashSet<IJavaProject>(), monitor);
					} catch (JavaModelException e) {
						throw new InvocationTargetException(e);
					}
				}
			});
			return result[0];
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
