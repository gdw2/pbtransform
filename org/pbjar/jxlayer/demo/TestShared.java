/**
 * Copyright (c) 2008-2009, Piet Blok
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided
 *     with the distribution.
 *   * Neither the name of the copyright holder nor the names of the
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.pbjar.jxlayer.demo;

import java.util.List;

import javax.swing.Action;

import java.io.IOException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import org.jdesktop.jxlayer.JXLayer;
import org.pbjar.jxlayer.plaf.misc.GeneralLayerUI;
import org.pbjar.jxlayer.plaf.misc.HideCursorUI;
import org.pbjar.jxlayer.plaf.misc.MagnifierUI;
import org.pbjar.jxlayer.plaf.misc.MouseDrawingUI;

/**
 * A demonstration of the shared use of LayerUI's by multiple JXLayer's.
 * 
 * <p>
 * Run a web start demo: <a
 * href="http://www.pbjar.org/blogs/jxlayer/jxlayer40/SharingDemo.jnlp"> <IMG
 * style="CLEAR: right" alt="Web Start Shared JXLayer"
 * src="http://javadesktop.org/javanet_images/webstart.small2.gif"
 * align="middle" border="1" /> </a>
 * </p>
 * 
 * @author Piet Blok
 */
public class TestShared {

    /**
     * Runs the program.
     * 
     * @param args
     *            not used
     */
    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {

	    @Override
	    public void run() {
		try {
		    new TestShared().test();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    private TestShared() {

    }

    private void addActions(JMenu parentMenu, String name,
	    List<Action> actionList) {
	JMenu menu;
	if (actionList.size() > 1) {
	    menu = new JMenu(name);
	    parentMenu.add(menu);

	} else {
	    menu = parentMenu;
	}
	for (Action action : actionList) {
	    if (action.getValue(Action.SELECTED_KEY) != null) {
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(action);
		menu.add(item);
	    } else {
		menu.add(action);
	    }
	}
    }

    @SuppressWarnings("unchecked")
    private JComponent createTarget(JFrame frame, String id, JMenuBar menubar,
	    GeneralLayerUI<JComponent, ?>[] layerUIs) {
	JTextPane originalComponent = new JTextPane() {

	    private static final long serialVersionUID = 1L;

	    @Override
	    public boolean getScrollableTracksViewportWidth() {
		return true;
	    }

	};
	try {
	    originalComponent.setPage(this.getClass().getResource(
		    "SharedTest.html"));
	} catch (IOException e) {
	    e.printStackTrace();
	}

	originalComponent.setEditable(false);

	JMenu menu = new JMenu(id);
	menubar.add(menu);

	JComponent wrappingTarget = originalComponent;
	for (GeneralLayerUI<JComponent, ?> layerUI : layerUIs) {
	    wrappingTarget = new JXLayer<JComponent>(wrappingTarget, layerUI);
	    addActions(menu, layerUI.getName(), layerUI
		    .getActions((JXLayer<JComponent>) wrappingTarget));
	}

	return new JScrollPane(wrappingTarget);
    }

    @SuppressWarnings("unchecked")
    private void test() throws IOException {
	JFrame frame = new JFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(600, 400);
	frame.setLocationRelativeTo(null);
	JMenuBar menubar = new JMenuBar();
	frame.setJMenuBar(menubar);

	GeneralLayerUI<JComponent, ?>[] layerUIs = new GeneralLayerUI[] {

	new MouseDrawingUI(),

	new HideCursorUI(500),

	new MagnifierUI(),

	};

	JMenu menu = new JMenu("General UI");
	menubar.add(menu);
	for (GeneralLayerUI layerUI : layerUIs) {
	    addActions(menu, layerUI.getName(), layerUI.getActions());
	}

	JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		createTarget(frame, "Left", menubar, layerUIs), createTarget(
			frame, "Right", menubar, layerUIs));
	splitPane.setDividerLocation(frame.getWidth() / 2);

	frame.add(splitPane);
	frame.setVisible(true);
    }

}
