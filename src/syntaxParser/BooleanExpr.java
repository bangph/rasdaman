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

class BooleanExpr implements IParseTreeNode {	
	CoverageExpr coverageExpr;
	String operator;
	int integerExpr;
	
	public BooleanExpr( CoverageExpr ce, String op ){	
		coverageExpr = ce;
		operator = op;
	}
	
	public BooleanExpr( CoverageExpr ce, int ie, String op ){	
		coverageExpr = ce;
		integerExpr = ie;
		operator = op;
	}
	
	public String toXML(){
		String result="";
		result += "<"+operator+">";
		if (operator.equals("bit")) {
			result += "<position>" + integerExpr +"<position>";
		}
		result += coverageExpr.toXML();			
		result += "</"+operator+">";
		return result;
	}
}
