from fastapi import FastAPI, Response
from pydantic import BaseModel
from resume_render import render_template, SkillsDict
from weasyprint import HTML
from fastapi.middleware.cors import CORSMiddleware

def weasyprint_render(html: str):
    return HTML(string=html).render().write_pdf()

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:8280"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class HtmlRender(BaseModel):
    body: str

#TODO maybe dynamic filename based on hash of content or something or other?
pdf_headers = {"Content-Disposition": "inline; filename=Rendered.pdf"}

def PdfResponse(pdf_bytes):
    return Response(pdf_bytes, media_type="application/pdf", headers=pdf_headers)

@app.post("/render_raw")
async def render_pdf(html: HtmlRender):
    pdf_bytes = weasyprint_render(html.body)
    return PdfResponse(pdf_bytes)

class ResumeRender(BaseModel):
    skills: SkillsDict

# TODO more robust, command-line arg/dependency injection(?)-based thing!
template:str
with open("/home/michael/Documents/JobSearch/Resume_Template.html") as f:
    template = f.read()

@app.post("/render_resume")
async def render_resume(input: ResumeRender):
    full_resume_text = render_template(template, input.skills)
    pdf_bytes = weasyprint_render(full_resume_text)
    return PdfResponse(pdf_bytes)

# Extra endpoints for "testing and experimentation"

from fastapi.responses import HTMLResponse
@app.post("/resume.html")
async def resume_html(input: ResumeRender):
    html_text = render_template(template, input.skills)
    return HTMLResponse(content=html_text)

@app.get("/resume_template")
async def resume_template():
    return template;