/*
 * This file is part of Petascope.
 *
 * Petascope is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Petascope is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Petascope. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information please see <http://www.Petascope.org>
 * or contact Peter Baumann via <baumann@rasdaman.com>.
 *
 * Copyright 2009 Jacobs University Bremen, Peter Baumann.
 */

package syntaxParser;

class CoverageConstructorExpr implements IParseTreeNode {
	String name;
	VariableList variables;
	ScalarExpr expr;

	public CoverageConstructorExpr(String name, VariableList vl, ScalarExpr expr) {
		this.name = name;
		this.variables = vl;
		this.expr = expr;
	}

	public String toXML() {
		return "<construct><coverage>" + name + "</coverage>" + variables.toXML() + expr.toXML() + "</construct>";
	}

}
