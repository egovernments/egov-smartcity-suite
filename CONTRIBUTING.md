_Have something you'd like to contribute to the eGov ERP?  We welcome pull
requests but ask that you carefully read this document first to understand how
best to submit them; what kind of changes are likely to be accepted; and what
to expect from the eGov team when evaluating your submission._

_Please refer back to this document as a checklist before issuing any pull
request; this will save time for everyone!_

## Code of Conduct
This project adheres to the Contributor Covenant [code of conduct](CODE_OF_CONDUCT.adoc).
By participating, you  are expected to uphold this code. Please report unacceptable behavior to contact@egovernments.org.

## Take Your First Steps

### Understand the basics

Not sure what a pull request is, or how to submit one? Take a look at GitHub's
excellent [help documentation][] first.

### Search our Gitter channel first; discuss if necessary

If you're unsure why something isn't working or wondering if there is a better
way of doing it please check on [Gitter][] first and if necessary start
a discussion. This is the official list of
[eGov ERP FAQ](https://confluence.egovernments.org/display/FAQ/FAQ). In short the issue tracker
should be used to report issues and make feature requests.

### Search JIRA; create an issue if necessary

Is there already an issue that addresses your concern? Do a bit of searching
in our [JIRA issue tracker][] to see if you can find something similar. If you
do not find something similar, please create a new JIRA issue before submitting
a pull request unless the change is truly trivial -- for example: typo fixes,
removing compiler warnings, etc.

### Sign the Contributor License Agreement (CLA)

If you have not previously done so, please sign the [Contributor License Agreement][].
If you forget to do so, you'll be reminded when you submit a pull request.

## Create a Branch

### Branch from `develop`

Develop currently represents work toward eGov ERP 3.0. Please submit
all pull requests there, even bug fixes and minor improvements. Backports to
`2.x` will be considered on a case-by-case basis.


### Use short branch names

Branches used when submitting pull requests should preferably be named
according to JIRA issues, e.g. 'PHOENIX-1234'. Otherwise, use succinct, lower-case,
dash (-) delimited names, such as 'fix-warnings', 'fix-typo', etc. In
[fork-and-edit][] cases, the GitHub default 'patch-1' is fine as well. This is
important, because branch names show up in the merge commits that result from
accepting pull requests and should be as expressive and concise as possible.

## Use eGov ERP Code Style

The complete [eGov ERP Code Style][] reference is available on the confluence, but
here's a quick summary:

### Mind the whitespace

Please carefully follow the whitespace and formatting conventions already
present in the framework.

1. Tabs, not spaces
1. Unix (LF), not DOS (CRLF) line endings
1. Eliminate all trailing whitespace
1. Wrap Javadoc at 90 characters
1. Aim to wrap code at 90 characters, but favor readability over wrapping
1. Preserve existing formatting; i.e. do not reformat code for its own sake
1. Search the codebase using `git grep` and other tools to discover common
    naming conventions, etc.
1. UTF-8 encoding for Java sources


### Add GPL 3 license header to all new classes

```java
/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package ...;
```

### Update GPL license header in modified files as necessary

Always check the date range in the license header. For example, if you've
modified a file in 2017 whose header still reads:

```java
/*
 * Copyright 2002-2011 the original author or authors.
```

Then be sure to update it to 2017 accordingly:

```java
/*
 * Copyright 2002-2017 the original author or authors.
```

### Use @since tags for newly-added public API types and methods

For example:

```java
/**
 * ...
 *
 * @author First Last
 * @since 5.0
 * @see ...
 */
```

## Prepare Your Commit

### Submit JUnit test cases for all behavior changes

Search the codebase to find related tests and add additional `@Test` methods
as appropriate. It is also acceptable to submit test cases on a per JIRA issue
basis, for example:

```java
package org.egov.infra.service;

/**
 * Unit tests for PHOENIX-1234, check custom beans are used after null checks
 * @author <Your Name>
 */
public class Phoenix1234Tests {

    @Test
    public void testValueNotNullPhoenix1234() {
        // ...
    }
}
```


### Squash commits

Use `git rebase --interactive --autosquash`, `git add --patch`, and other tools
to "squash" multiple commits into a single atomic commit. In addition to the man
pages for git, there are many resources online to help you understand how these
tools work. The [Rewriting History section of Pro Git][] provides a good overview.


### Use real name in git commits

Please configure git to use your real first and last name for any commits you
intend to submit as pull requests. For example, this is not acceptable:

    Author: Nickname <user@mail.com>

Rather, please include your first and last name, properly capitalized, as
submitted against the eGov Individual Contributor License Agreement (ICLA):

    Author: First Last <user@mail.com>

This helps ensure traceability against the ICLA and also goes a long way to
ensuring useful output from tools like `git shortlog` and others.

You can configure this via the account admin area in GitHub (useful for
fork-and-edit cases); _globally_ on your machine with

    git config --global user.name "First Last"
    git config --global user.email user@mail.com

or _locally_ for the `egov` repository only by omitting the
'--global' flag:

    cd egov
    git config user.name "First Last"
    git config user.email user@mail.com


### Format commit messages

Please read and follow the [Commit Guidelines section of Pro Git][].

Most importantly, please format your commit messages in the following way
(adapted from the commit template in the link above):

    Short (50 chars or less) summary of changes
    Prefix with Issue Id if available like [PHOENIX-1234] for multiple issues [PHOENIX-1234,PHOENIX-1235]
    More detailed explanatory text, if necessary. Wrap it to about 72
    characters or so. In some contexts, the first line is treated as the
    subject of an email and the rest of the text as the body. The blank
    line separating the summary from the body is critical (unless you omit
    the body entirely); tools like rebase can get confused if you run the
    two together.

    Further paragraphs come after blank lines.

     - Bullet points are okay, too

     - Typically a hyphen or asterisk is used for the bullet, preceded by a
       single space, with blank lines in between, but conventions vary here
    


1. Use imperative statements in the subject line, e.g. "Fix broken Javadoc link".
1. Begin the subject line with a capitalized verb, e.g. "Add, Prune, Fix,
    Introduce, Avoid, etc."
1. Do not end the subject line with a period.
1. Restrict the subject line to 50 characters or less if possible.
1. Wrap lines in the body at 72 characters or less.
1. Mention associated JIRA issue(s) at the end of the commit comment, prefixed
    with "Issue: " as above.
1. In the body of the commit message, explain how things worked before this
    commit, what has changed, and how things work now.

For examples of this style, issue a `git log --author=egov-systems` in the
`egov` git repository. For convenience, here are several such commits:

- https://github.com/egovernments/eGov/commit/f2550dd038ed2c0f6e3da029266b389627e064c9
- https://github.com/egovernments/eGov/commit/6f843919b8628be011c16f1f71f9bb91018bc122
## Run the Final Checklist

### Run all tests prior to submission

See the [building from source][] section of the `README` for instructions. Make
sure that all tests pass prior to submitting your pull request.


### Submit your pull request

Subject line:

Follow the same conventions for pull request subject lines as mentioned above
for commit message subject lines.

In the body:

1. Explain your use case. What led you to submit this change? Why were existing
    mechanisms in the framework insufficient? Make a case that this is a
    general-purpose problem and that yours is a general-purpose solution, etc.
1. Add any additional information and ask questions; start a conversation or
    continue one from JIRA.
1. Mention the JIRA issue ID.
1. Also mention that you have submitted the ICLA as described above.

Note that for pull requests containing a single commit, GitHub will default the
subject line and body of the pull request to match the subject line and body of
the commit message. This is fine, but please also include the items above in the
body of the request.


### Mention your pull request on the associated JIRA issue

Add a comment to the associated JIRA issue(s) linking to your new pull request.


### Expect discussion and rework

The eGov team takes a very conservative approach to accepting contributions to
the Urban ERP. This is to keep code quality and stability as high as possible,
and to keep complexity at a minimum. Your changes, if accepted, may be heavily
modified prior to merging. You will retain "Author:" attribution for your Git
commits granted that the bulk of your changes remain intact. You may be asked to
rework the submission for style (as explained above) and/or substance. Again, we
strongly recommend discussing any serious submissions with the eGov ERP 
team _prior_ to engaging in serious development work.

Note that you can always force push (`git push -f`) reworked / rebased commits
against the branch used to submit your pull request. In other words, you do not
need to issue a new pull request when asked to make changes.

[help documentation]: https://help.github.com/categories/collaborating-with-issues-and-pull-requests/
[JIRA issue tracker]: https://issues.egovernments.org/browse/PHOENIX
[Contributor License Agreement]: https://www.egovernments.org
[fork-and-edit]: https://github.com/blog/844-forking-with-the-edit-button
[eGov ERP Code Style]: https://confluence.egovernments.org/display/ES/Coding+Standards
[Rewriting History section of Pro Git]: http://git-scm.com/book/en/Git-Tools-Rewriting-History
[Commit Guidelines section of Pro Git]: http://git-scm.com/book/en/Distributed-Git-Contributing-to-a-Project#Commit-Guidelines
[building from source]: https://github.com/egovernments/eGov/blob/master/README.md#building-source
[Gitter]: https://gitter.im/egovernments/eGov
