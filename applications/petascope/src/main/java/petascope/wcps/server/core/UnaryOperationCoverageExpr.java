/*
 * This file is part of rasdaman community.
 *
 * Rasdaman community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Rasdaman community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with rasdaman community.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2003 - 2010 Peter Baumann / rasdaman GmbH.
 *
 * For more information please see <http://www.rasdaman.org>
 * or contact Peter Baumann via <baumann@rasdaman.com>.
 */
package petascope.wcps.server.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import petascope.exceptions.WCPSException;
import org.w3c.dom.*;

public class UnaryOperationCoverageExpr implements IRasNode, ICoverageInfo {
    
    private static Logger log = LoggerFactory.getLogger(UnaryOperationCoverageExpr.class);

    private CoverageExpr child;
    private CoverageInfo info;
    private String operation;
    private String params;

    public UnaryOperationCoverageExpr(Node node, XmlQuery xq)
            throws WCPSException {
        String nodeName = node.getNodeName();
        log.trace(nodeName);

        if (nodeName.equals("unaryPlus")) {
            operation = "+";
            child = new CoverageExpr(node.getFirstChild(), xq);
        } else if (nodeName.equals("unaryMinus")) {
            operation = "-";
            child = new CoverageExpr(node.getFirstChild(), xq);
        } else if (nodeName.equals("sqrt") || nodeName.equals("abs")
                || nodeName.equals("exp") || nodeName.equals("log") || nodeName.equals("ln")
                || nodeName.equals("sin") || nodeName.equals("cos") || nodeName.equals("tan")
                || nodeName.equals("sinh") || nodeName.equals("cosh")
                || nodeName.equals("tanh") || nodeName.equals("arcsin")
                || nodeName.equals("arccos") || nodeName.equals("arctan")
                || nodeName.equals("not") || nodeName.equals("re") || nodeName.equals("im")) {
            operation = nodeName;
            child = new CoverageExpr(node.getFirstChild(), xq);
        } else if (nodeName.equals("bit")) {
            operation = "bit";
            Node c = node.getFirstChild();

            while (c != null) {
                if (c.getNodeName().equals("#text")) {
                    c = c.getNextSibling();
                    continue;
                }

                if (c.getNodeName().equals("bitIndex")) {
                    try {
                        params = c.getFirstChild().getNodeValue();
                        int i = Integer.parseInt(params);
                        log.trace("Found bitIndex = " + params);
                    } catch (NumberFormatException e) {
                        throw new WCPSException("Invalid Number as bitIndex: " + params);
                    }
                } else {
                    child = new CoverageExpr(c, xq);
                }

                c = c.getNextSibling();
            }
        } else if (nodeName.equals("cast")) {
            operation = "cast";
            Node c = node.getFirstChild();

            while (c != null) {
                log.trace("  child name: " + c.getNodeName());
                if (c.getNodeName().equals("#text")) {
                    c = c.getNextSibling();
                    continue;
                }

                if (c.getNodeName().equals("type")) {
                    RangeField typeNode = new RangeField(c, xq);
                    params = typeNode.toRasQL();
                } else {
                    child = new CoverageExpr(c, xq);
                }

                c = c.getNextSibling();
            }
        } else if (nodeName.equals("fieldSelect")) {
            operation = "select";
            Node c = node.getFirstChild();

            while (c != null) {
                if (c.getNodeName().equals("#text")) {
                    c = c.getNextSibling();
                    continue;
                }

                if (c.getNodeName().equals("field")) {
                    FieldName nameNode = new FieldName(c.getFirstChild(), xq);
                    params = nameNode.toRasQL();
                } else {
                    child = new CoverageExpr(c, xq);
                }

                c = c.getNextSibling();
            }
        } else {
            throw new WCPSException("Unknown unary operation: " + nodeName);
        }

        info = new CoverageInfo(child.getCoverageInfo());
        log.trace("  operation: " + operation);
    }

    public CoverageInfo getCoverageInfo() {
        return info;
    }

    public String toRasQL() {
        if (operation.equals("sqrt") || operation.equals("abs") || operation.equals("exp")
                || operation.equals("log") || operation.equals("ln") || operation.equals("sin")
                || operation.equals("cos") || operation.equals("tan")
                || operation.equals("sinh") || operation.equals("cosh")
                || operation.equals("tanh") || operation.equals("arcsin")
                || operation.equals("arccos") || operation.equals("arctan")
                || operation.equals("not") || operation.equals("+") || operation.equals("-")) {
            return operation + "(" + child.toRasQL() + ")";
        } else if (operation.equals("cast")) {
            // Use rasql's direct "type-casting" facility for constant scalar expressions
            // For example, (char)1 does not work, but 1c is a valid expression.
            if (child.isScalarExpr() && params.equals("char"))
                return child.toRasQL() + "c";
            else
                return "(" + params + ")(" + child.toRasQL() + ")";
        } else if (operation.equals("select")) {
            return "(" + child.toRasQL() + ")." + params;
        } else if (operation.equals("bit")) {
            return "bit(" + child.toRasQL() + "," + params + ")";
        }

        return " error ";
    }
}
