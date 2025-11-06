import React, { useState, useEffect } from 'react';
import axios from 'axios';

export default function UCFRDashboard(){
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);
  useEffect(()=> {
    // preload sample
    fetch('/api/v1/consumer/CUST-1001/canonical').then(r=>{
      if(r.ok) return r.json();
      return null;
    }).then(j=>{ if(j) setResult(j); });
  },[]);
  const run = async () => {
    setLoading(true);
    try{
      const sample = await fetch('/sample_reports.json').then(r=>r.json());
      const res = await axios.post('/api/v1/reconcile', sample);
      setResult(res.data);
    }catch(e){
      console.error(e);
      alert('Error running reconciliation');
    }finally{ setLoading(false); }
  };
  return (
    <div style={{fontFamily:'Arial',padding:20}}>
      <h1>UCFR Dashboard</h1>
      <p>Demo React dashboard for reconciliation</p>
      <button onClick={run} disabled={loading}>{loading? 'Working...':'Run Reconciliation'}</button>
      {result && (
        <div style={{marginTop:20}}>
          <h3>Consumer: {result.consumerToken} (ts: {result.timestamp})</h3>
          {result.canonicalRecords.map((c,idx)=>(
            <div key={idx} style={{border:'1px solid #ddd',padding:10,marginBottom:10}}>
              <p><strong>Canonical Balance:</strong> {c.canonicalBalance}</p>
              <p><strong>Status:</strong> {c.canonicalStatus}</p>
              <p><strong>Confidence:</strong> {(c.confidence*100).toFixed(1)}%</p>
              <p><strong>Explain:</strong> {c.explain}</p>
              <details>
                <summary>Linked Records</summary>
                <pre>{JSON.stringify(c.linkedRecords,null,2)}</pre>
              </details>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
