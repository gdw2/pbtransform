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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.geom.Ellipse2D;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.Timer;

/**
 * A very simple animated icon to see what happens in the Transform demo.
 * 
 * @author Piet Blok
 */
public class AnimatedIcon implements Icon {

    private static final int maxRadius = 4;

    private Map<Component, Object> componentMap = new WeakHashMap<Component, Object>();

    private double radius = 0;

    private Ellipse2D dot = new Ellipse2D.Float();

    public AnimatedIcon() {

	Timer timer = new Timer(100, new AbstractAction() {

	    private static final long serialVersionUID = 1L;

	    @Override
	    public void actionPerformed(ActionEvent e) {
		radius += 0.1;
		if (radius > maxRadius) {
		    radius = 0.1;
		}
		dot.setFrameFromCenter(maxRadius, maxRadius,
			maxRadius - radius, maxRadius - radius);
		// For all registered components
		Iterator<Component> iterator = componentMap.keySet().iterator();
		while (iterator.hasNext()) {
		    iterator.next().repaint();
		    // Remove, because the component will be re-registered
		    // when painting, or not when not painting.
		    iterator.remove();
		}
	    }
	});
	timer.start();
    }

    @Override
    public int getIconHeight() {
	return 2 * maxRadius;
    }

    @Override
    public int getIconWidth() {
	return 2 * maxRadius;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
	// Remember the component for a repaint
	componentMap.put(c, null);
	// Paint the icon
	((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	g.translate(x, y);
	((Graphics2D) g).fill(dot);
    }

}