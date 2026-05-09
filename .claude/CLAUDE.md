# CLAUDE.md

Test cases

Every PR push must include a test plan. Format each case as a checkbox with
a clear action and expected result:

• [ ] Do X → Expected: Y

After every follow-up change, restate the full test plan so it is always current.
Do not carry over stale cases from a previous iteration.

Before implementing

• Audit the repo first. Do not add what is already there.
• For Create integrations, read the Create source (submodule) directly.
  Do not reconstruct behavior from description or assumptions.
• Verify domain constraints before writing test cases (e.g. can this state
  actually exist in the world?).

Squash commits

When a PR is ready to merge, provide a single squash commit message on request.
